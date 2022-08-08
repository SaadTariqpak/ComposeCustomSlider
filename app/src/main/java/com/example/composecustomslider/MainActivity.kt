package com.example.composecustomslider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composecustomslider.ui.theme.BlackDark
import com.example.composecustomslider.ui.theme.BlueHandle
import com.example.composecustomslider.ui.theme.ComposeCustomSliderTheme
import com.example.composecustomslider.ui.theme.Green600
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author: Saad Tariq
 * @Date: 8/7/22
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCustomSliderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Green600

                ) {
                    CustomSlider()
                }
            }
        }
    }
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = Math.toDegrees(theta)

    if (angle < 0) {
        angle += 360.0
    }
    return angle
}

@Composable
fun CustomSlider() {


    var mRadius by remember { mutableStateOf(0f) } //circle radius
    var handleCenter by remember { mutableStateOf(Offset.Zero) }
    var angle by remember {
        mutableStateOf(0.0)
    }
    var shapeCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    val strokeWidth = 5.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(BlackDark)
                .align(Alignment.Center)

        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                mRadius = (size.width / 2) - 50
                drawCircle(
                    color = Color.White,
                    radius = mRadius,
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 71f), 0f)
                    )

                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        ) {

            Canvas(modifier = Modifier
                .fillMaxSize()
                .rotate(-90f)//rotating canvas to change quadrants for circle and arc
                .pointerInput(Unit) {

                    detectDragGestures { change, dragAmount ->
                        handleCenter += dragAmount
                        val rotAngle = getRotationAngle(
                            handleCenter,
                            shapeCenter
                        )
                        angle = rotAngle
                        change.consumeAllChanges()

                    }
                }
            ) {

                shapeCenter = center
                val x =
                    (shapeCenter.x + cos(Math.toRadians(angle)) * mRadius).toFloat()
                val y =
                    (shapeCenter.y + sin(Math.toRadians(angle)) * mRadius).toFloat()

                handleCenter = Offset(x, y)

                drawArc(
                    color = if (angle < 180) BlueHandle else Color.Red,
                    startAngle = 0f,
                    sweepAngle = angle.toFloat(),
                    useCenter = false,
                    topLeft = Offset(50f, 50f),
                    size = Size(size.width - 100, size.height - 100),
                    style = Stroke(
                        strokeWidth.toPx(), cap = StrokeCap.Round
                    )
                )

                drawCircle(
                    color = if (angle < 180) BlueHandle else Color.Red,
                    center = handleCenter,
                    radius = 25f
                )
            }
            Text(
                text = (angle.toInt() / 10).toString() + "°",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

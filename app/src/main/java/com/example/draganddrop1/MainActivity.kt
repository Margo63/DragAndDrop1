package com.example.draganddrop1

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.draganddrop1.ui.theme.DragAndDrop1Theme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DragAndDrop1Theme {
                Column {
                    Canva(windowManager)
                }
                
            }
        }
    }
}

//самый простой пример с перетаскиванием куба
@Composable
private fun DragCube() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(150.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            Toast
                                .makeText(context, "start", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        },
                        onDragEnd = {
                            Toast
                                .makeText(context, "stop", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
        )
    }
}

//шаблон для баксов
@Composable
private fun DragBox(offsetX:Float, offsetY: Float,color: Color, onDragStart:(Offset)->Unit, onDrag:(change: PointerInputChange, dragAmount: Offset)->Unit, onDragEnd:()->Unit){
    Box(
        Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(color)
            .size(50.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = onDragStart,
                    onDrag = onDrag,
                    onDragEnd = onDragEnd
                )
            }
    )

}

//провальная попытка
@Composable
private fun DragCabels() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX1 by remember { mutableStateOf(0f) }
        var offsetY1 by remember { mutableStateOf(0f) }
        var offsetX2 by remember { mutableStateOf(0f) }
        var offsetY2 by remember { mutableStateOf(260f) }
        var offsetX3 by remember { mutableStateOf(0f) }
        var offsetY3 by remember { mutableStateOf(630f) }
        var currentCube  by remember { mutableStateOf(0) }
        DragBox(offsetX1, offsetY1, Color.Blue,
            onDragStart = {
                currentCube = 1
            },
            onDrag={ change, dragAmount ->
                change.consume()
                offsetX1 += dragAmount.x
                offsetY1 += dragAmount.y
            },
            onDragEnd ={
                Toast
                    .makeText(context, "stop", Toast.LENGTH_SHORT)
                    .show()
            }
            )
        DragBox(offsetX2, offsetY2, Color.Red,
            onDragStart = {
                currentCube = 2
            },
            onDrag={ change, dragAmount ->
                change.consume()
                offsetX2 += dragAmount.x
                offsetY2 += dragAmount.y
            },
            onDragEnd ={
                Toast
                    .makeText(context, "stop", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        DragBox(offsetX3, offsetY3, Color.Yellow,
            onDragStart = {
                currentCube = 3
            },
            onDrag={ change, dragAmount ->
                change.consume()
                offsetX3 += dragAmount.x
                offsetY3 += dragAmount.y
            },
            onDragEnd ={
                Toast
                    .makeText(context, "stop", Toast.LENGTH_SHORT)
                    .show()
            }
        )


    }
}

@Composable
private fun Canva(windowManager:WindowManager){
    //для вывода сообщений
    val context = LocalContext.current
    //получение размеров в dp
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    //получение размеров в пикселях
    val density = LocalDensity.current
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels

    // начальные координаты
    var offsetX1 by remember { mutableStateOf(0f) }
    var offsetY1 by remember { mutableStateOf(0f) }
    var offsetX2 by remember { mutableStateOf(0f) }
    var offsetY2 by remember { mutableStateOf((height/3).dp.value) }
    var offsetX3 by remember { mutableStateOf(0f) }
    var offsetY3 by remember { mutableStateOf((height*2/3).dp.value) }
    Box(){
        Column(modifier = Modifier.fillMaxSize()) {
            // при попытке перетащить бокс будет рисоваться линия, если соеденино правильно, то все ок, иначе координаты меняются на начальные
            Box(
                Modifier
                    .background(Color.Blue)
                    .size(75.dp, screenHeight / 3)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX1 += dragAmount.x
                                offsetY1 += dragAmount.y
                            },
                            onDragEnd = {
                                if (offsetX1 >= width - with(density) { 75.dp.toPx() }  &&
                                    offsetY1>=0f && offsetY1<=height/3) {
                                    Toast
                                        .makeText(context, "ok", Toast.LENGTH_SHORT)
                                        .show()
                                }else{
                                    Toast
                                        .makeText(context, "wrong", Toast.LENGTH_SHORT)
                                        .show()
                                    offsetX1 = 0f
                                    offsetY1 = 0f
                                }
                            }
                        )
                    }
            )
            Box(
                Modifier
                    .background(Color.Red)
                    .size(75.dp, screenHeight / 3)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX2 += dragAmount.x
                                offsetY2 += dragAmount.y
                            },
                            onDragEnd = {
                                if (offsetX2 >= width - with(density) { 75.dp.toPx() }  &&
                                    offsetY2>=height/3 && offsetY2<=height/3*2) {
                                    Toast
                                        .makeText(context, "ok", Toast.LENGTH_SHORT)
                                        .show()
                                }else{
                                    Toast
                                        .makeText(context, "wrong", Toast.LENGTH_SHORT)
                                        .show()
                                    offsetX2 = 0f
                                    offsetY2 = (height/3).dp.value
                                }
                            }
                        )
                    }
            )
            Box(
                Modifier
                    .background(Color.Green)
                    .size(75.dp, screenHeight / 3)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX3 += dragAmount.x
                                offsetY3 += dragAmount.y
                            },
                            onDragEnd = {
                                if (offsetX3 >= width - with(density) { 75.dp.toPx() }  &&
                                    offsetY3>=height/3*2 && offsetY3<=height) {
                                    Toast
                                        .makeText(context, "ok", Toast.LENGTH_SHORT)
                                        .show()
                                }else{
                                    Toast
                                        .makeText(context, "wrong", Toast.LENGTH_SHORT)
                                        .show()
                                    offsetX3 = 0f
                                    offsetY3 = (height*2/3).dp.value
                                }
                            }
                        )
                    }
            )
        }
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End) {
            Box(
                Modifier
                    .background(Color.Blue)
                    .size(75.dp, screenHeight / 3)
            )
            Box(
                Modifier
                    .background(Color.Red)
                    .size(75.dp, screenHeight / 3)

            )
            Box(
                Modifier
                    .background(Color.Green)
                    .size(75.dp, screenHeight / 3)

            )
        }
        Canvas(modifier = Modifier.fillMaxSize()) {

            val center =size.height/6
            drawLine(color = Color.Blue,
                Offset(0f,0f + center),Offset(offsetX1,offsetY1), strokeWidth = 10f)
            drawLine(color = Color.Red,
                Offset(0f,size.height/3 + center),Offset(offsetX2,offsetY2), strokeWidth = 10f)
            drawLine(color = Color.Green,
                Offset(0f,size.height*2/3 + center),Offset(offsetX3,offsetY3), strokeWidth = 10f)

        }
    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun DragInteractionSample() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center,
//        content = {
//
//            val interactionSource = remember { MutableInteractionSource() }
//            val interactions = remember { mutableStateListOf<Interaction>() }
//            var text by remember { mutableStateOf("") }
//
//            LaunchedEffect(interactionSource) {
//                interactionSource.interactions.collect { interaction ->
//                    when (interaction) {
//                        is DragInteraction.Start -> {
//                            text = "Drag Start"
//                        }
//                        is DragInteraction.Stop -> {
//                            text = "Drag Stop"
//                        }
//                        is DragInteraction.Cancel -> {
//                            text = "Drag Cancel"
//                        }
//                    }
//                }
//            }
//
//            val coroutineScope = rememberCoroutineScope()
//
//            var offsetX by remember { mutableStateOf(0f) }
//            var offsetY by remember { mutableStateOf(0f) }
//
//            val modifier = Modifier
//                .offset {
//                    IntOffset(
//                        x = offsetX.roundToInt(),
//                        y = offsetY.roundToInt()
//                    )
//                }
//                .size(60.dp)
//                .pointerInput(Unit) {
//
//                    var interaction: DragInteraction.Start? = null
//                    detectDragGestures(
//                        onDragStart = {
//                            coroutineScope.launch {
//                                interaction = DragInteraction.Start()
//                                interaction?.run {
//                                    interactionSource.emit(this)
//                                }
//
//                            }
//                        },
//                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
//                            offsetX += dragAmount.x
//                            offsetY += dragAmount.y
//
//                        },
//                        onDragCancel = {
//                            coroutineScope.launch {
//                                interaction?.run {
//                                    interactionSource.emit(DragInteraction.Cancel(this))
//                                }
//                            }
//                        },
//                        onDragEnd = {
//                            coroutineScope.launch {
//                                interaction?.run {
//                                    interactionSource.emit(DragInteraction.Stop(this))
//                                }
//                            }
//                        }
//                    )
//                }
//
//            Surface(
//                modifier = modifier,
//                interactionSource = interactionSource,
//                onClick = {},
//                content = {},
//                color = MaterialTheme.colorScheme.primary
//            )
//
//            Text(text = text)
//        }
//    )
//}
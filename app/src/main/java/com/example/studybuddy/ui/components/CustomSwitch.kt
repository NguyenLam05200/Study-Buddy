package com.example.studybuddy.ui.components
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.studybuddy.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        CustomSwitch(
            height = 70.dp,
            width = 140.dp,
            circleButtonPadding = 4.dp,
            outerBackgroundOnResource = R.drawable.switch_body_night,
            outerBackgroundOffResource = R.drawable.switch_body_day,
            circleBackgroundOnResource = R.drawable.switch_btn_moon,
            circleBackgroundOffResource = R.drawable.switch_btn_sun,
            stateOn = 1,
            stateOff = 0,
            initialValue = 0,
            onCheckedChanged = {}
        )


    }

}

@Preview
@Composable
fun MainPrev() {
    StudyBuddyTheme {
        Main()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@Composable
fun CustomSwitch(
    height: Dp,
    width: Dp,
    circleButtonPadding: Dp,
    outerBackgroundOnResource: Int,
    outerBackgroundOffResource: Int,
    circleBackgroundOnResource: Int,
    circleBackgroundOffResource: Int,
    stateOn: Int,
    stateOff: Int,
    initialValue: Int,
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue)

    // Đồng bộ swipeableState với initialValue khi nó thay đổi từ bên ngoài
    LaunchedEffect(initialValue) {
        swipeableState.snapTo(initialValue)
    }

    // Theo dõi thay đổi của swipeableState và gọi onCheckedChanged
    LaunchedEffect(swipeableState.currentValue) {
        if (swipeableState.currentValue == stateOn) {
            onCheckedChanged(true) // Chuyển sang trạng thái "on"
        } else if (swipeableState.currentValue == stateOff) {
            onCheckedChanged(false) // Chuyển sang trạng thái "off"
        }
    }

    val sizePx = with(LocalDensity.current) { (width - height).toPx() }
    val anchors = mapOf(0f to stateOff, sizePx to stateOn)

    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(height))
            .border(1.dp, Color.DarkGray, CircleShape)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.2f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Transparent)
            .then(
                if (swipeableState.currentValue == stateOff) Modifier.paint(
                    painterResource(id = outerBackgroundOffResource),
                    contentScale = ContentScale.FillBounds
                ) else Modifier.paint(
                    painterResource(id = outerBackgroundOnResource),
                    contentScale = ContentScale.FillBounds
                )
            )
            .clickable {
                scope.launch {
                    if (swipeableState.currentValue == stateOff) {
                        swipeableState.animateTo(stateOn)
                    } else {
                        swipeableState.animateTo(stateOff)
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(height)
                .padding(circleButtonPadding)
                .clip(RoundedCornerShape(50))
                .then(
                    if (swipeableState.currentValue == stateOff) Modifier.paint(
                        painterResource(id = circleBackgroundOffResource),
                        contentScale = ContentScale.FillBounds
                    ) else Modifier.paint(
                        painterResource(id = circleBackgroundOnResource),
                        contentScale = ContentScale.FillBounds
                    )
                )
        )
    }
}




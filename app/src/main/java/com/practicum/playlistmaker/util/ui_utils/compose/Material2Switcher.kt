package com.practicum.playlistmaker.settings.ui.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R

@Composable
fun Material2Switcher(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth = 32.dp
    val trackHeight = 12.dp
    val thumbDiameter = 18.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) (trackWidth - thumbDiameter) else 0.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "thumb_animation"
    )

    val trackColor by animateColorAsState(
        targetValue = colorResource(if (checked) R.color.primary_container_color else R.color.pm_gray),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "track_color_animation"
    )

    val thumbColor by animateColorAsState(
        targetValue = colorResource(R.color.primary_color),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "thumb_color_animation"
    )

    Box(
        modifier = modifier
            .size(width = trackWidth, height = thumbDiameter)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!checked) }
    ) {
        Box(
            modifier = Modifier
                .size(width = trackWidth, height = trackHeight)
                .align(Alignment.Center)
                .background(
                    color = trackColor,
                    shape = RoundedCornerShape(percent = 50)
                )
        )

        Box(
            modifier = Modifier
                .size(thumbDiameter)
                .align(Alignment.CenterStart)
                .offset(x = thumbOffset)
                .background(thumbColor, CircleShape)
        )
    }
}
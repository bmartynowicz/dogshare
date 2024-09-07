package com.dogshare.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    state: SwipeableCardState = rememberSwipeableCardState(),
    modifier: Modifier = Modifier,
    onSwipe: (Direction) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        state.onDragEnd()
                        state.swipeDirection?.let { direction ->
                            onSwipe(direction)
                        }
                    },
                    onDrag = { _, dragAmount ->
                        state.onDrag(dragAmount)
                    }
                )
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(state.getOffset().x, state.getOffset().y)
                }
            }
    ) {
        content()
    }
}

enum class Direction { LEFT, RIGHT, UP, DOWN }
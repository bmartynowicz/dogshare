package com.dogshare.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Stable
class SwipeableCardState(
    private val scope: CoroutineScope
) {
    var offsetX by mutableStateOf(0f)
        private set

    var offsetY by mutableStateOf(0f)
        private set

    var isSwiped by mutableStateOf(false)
        private set

    var swipeDirection by mutableStateOf<Direction?>(null)
        private set

    fun reset() {
        offsetX = 0f
        offsetY = 0f
        isSwiped = false
        swipeDirection = null
    }

    fun onDrag(dragAmount: Offset) {
        offsetX += dragAmount.x
        offsetY += dragAmount.y
    }

    fun onDragEnd() {
        scope.launch {
            // Determine the swipe direction and mark the card as swiped
            swipeDirection = when {
                offsetX > 200 -> Direction.RIGHT
                offsetX < -200 -> Direction.LEFT
                offsetY > 200 -> Direction.DOWN
                offsetY < -200 -> Direction.UP
                else -> null
            }

            isSwiped = swipeDirection != null
        }
    }

    fun getOffset(): IntOffset {
        return IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
    }
}

@Composable
fun rememberSwipeableCardState(): SwipeableCardState {
    val scope = rememberCoroutineScope()
    return remember(scope) {
        SwipeableCardState(scope)
    }
}
package de.rauschdo.eschistory.ui.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import de.rauschdo.eschistory.DIALOG_ANIMATION_TIME
import de.rauschdo.eschistory.DIALOG_DELAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Inspired by…
// https://medium.com/tech-takeaways/ios-like-modal-view-dialog-animation-in-jetpack-compose-fac5778969af
// https://medium.com/bilue/expanding-dialog-in-jetpack-compose-a6be40deab86

/**
 * Helper to trigger animations from defined dialog content outside
 */
class AnimatedTransitionDialogHelper(
    private val coroutineScope: CoroutineScope,
    private val onDismissFlow: MutableSharedFlow<() -> Unit>
) {
    fun triggerAnimatedDismiss(toExecute: () -> Unit) {
        coroutineScope.launch {
            onDismissFlow.emit(toExecute)
        }
    }
}

@Composable
fun BaseDialogAnimated(
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    ),
    onDismissRequest: () -> Unit,
    content: @Composable (AnimatedTransitionDialogHelper?) -> Unit
) {
    AnimatedTransitionDialogWrapper(
        properties = properties,
        onDismissRequest = onDismissRequest,
        onCancelClickOutside = {
            onDismissRequest()
        }
    ) { animatedTransitionDialogHelper ->
        content(animatedTransitionDialogHelper)
    }
}

/**
 * Wrapper for custom animation support
 */
@Composable
private fun AnimatedTransitionDialogWrapper(
    onDismissRequest: () -> Unit,
    onCancelClickOutside: (() -> Unit)?,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    ),
    cancelable: Boolean = true,
    animateExit: Boolean = true,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (AnimatedTransitionDialogHelper?) -> Unit,
) {
    val onDismissSharedFlow: MutableSharedFlow<() -> Unit> = remember { MutableSharedFlow() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val animateTrigger = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_DELAY)
            animateTrigger.value = true
        }
        launch {
            onDismissSharedFlow.asSharedFlow().collectLatest {
                startDismissWithExitAnimation(animateTrigger, animateExit, toExecute = it)
            }
        }
    }

    Dialog(
        properties = properties,
        onDismissRequest = {
            coroutineScope.launch {
                startDismissWithExitAnimation(
                    animateTrigger,
                    animateExit,
                    toExecute = onDismissRequest
                )
            }
        }
    ) {
        var boxModifier: Modifier = if (cancelable) {
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    // don't wanna show ripple effect of clicked box
                    indication = null,
                    onClick = {
                        if (animateExit) {
                            coroutineScope.launch {
                                startDismissWithExitAnimation(
                                    animateTrigger,
                                    animateExit = true,
                                    toExecute = onCancelClickOutside ?: onDismissRequest
                                )
                            }
                        } else {
                            onCancelClickOutside?.invoke() ?: onDismissRequest()
                        }
                    }
                )
        } else {
            Modifier.fillMaxSize()
        }

        if (!properties.usePlatformDefaultWidth) {
            boxModifier = boxModifier.padding(horizontal = 16.dp)
        }

        Box(
            modifier = boxModifier,
            contentAlignment = contentAlignment,
        ) {
            AnimatedScaleInTransition(visible = animateTrigger.value) {
                if (animateExit) {
                    content(AnimatedTransitionDialogHelper(coroutineScope, onDismissSharedFlow))
                } else {
                    content(null)
                }
            }
        }
    }
}

private suspend fun startDismissWithExitAnimation(
    animateTrigger: MutableState<Boolean>,
    animateExit: Boolean,
    toExecute: () -> Unit
) {
    animateTrigger.value = false
    if (animateExit) {
        delay(DIALOG_ANIMATION_TIME)
    }
    toExecute()
}

@Composable
internal fun AnimatedScaleInTransition(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(DIALOG_ANIMATION_TIME.toInt())
        ),
        exit = scaleOut(
            animationSpec = tween(DIALOG_ANIMATION_TIME.toInt())
        ),
        content = content
    )
}

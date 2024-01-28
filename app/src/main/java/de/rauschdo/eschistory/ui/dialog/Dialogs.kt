package de.rauschdo.eschistory.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.rauschdo.eschistory.data.DialogModel
import de.rauschdo.eschistory.ui.main.BaseButton
import de.rauschdo.eschistory.ui.main.BaseHeader
import de.rauschdo.eschistory.ui.main.BaseOutlinedButton
import de.rauschdo.eschistory.ui.main.BaseText
import de.rauschdo.eschistory.ui.theme.TypeDefinition
import de.rauschdo.eschistory.ui.theme.baseCornerShape
import de.rauschdo.eschistory.ui.theme.defaultButtonHeight
import de.rauschdo.eschistory.ui.theme.heigherOf
import de.rauschdo.eschistory.utility.loremIpsum

/**
 * Centralized object to funnel through constructions/variants of Dialogs to display
 */
object Dialogs {

    fun sample(onDismissRequest: () -> Unit) = DialogModel(
        id = "sample",
        content = {
            BaseDialogAnimated(onDismissRequest = onDismissRequest) { dialogTransitionHelper ->
                val dismissClick = {
                    dialogTransitionHelper?.triggerAnimatedDismiss(
                        toExecute = onDismissRequest
                    ) ?: onDismissRequest
                }

                Card(shape = baseCornerShape) {
                    Column(modifier = Modifier) {
                        BaseHeader(
                            title = {
                                BaseText(
                                    text = loremIpsum(),
                                    style = TypeDefinition.titleMedium
                                )
                            },
                            actions = {
                                IconButton(onClick = onDismissRequest) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
                        )

                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .weight(weight = 1f, fill = false)
                        ) {
                            val localDensity = LocalDensity.current

                            // Sample scaling buttons (e.g through font/display scaling)
                            var negativeButtonHeight: Dp? by remember { mutableStateOf(null) }
                            var positiveButtonHeight: Dp? by remember { mutableStateOf(null) }
                            var preferedButtonHeight: Dp? by remember { mutableStateOf(null) }

                            preferedButtonHeight = heigherOf(
                                heigh1 = negativeButtonHeight,
                                heigh2 = positiveButtonHeight,
                                fallbackHeight = defaultButtonHeight
                            )

                            var negativeButtonModifier = Modifier
                                .weight(1f)
                                .onGloballyPositioned { coordinates ->
                                    negativeButtonHeight =
                                        with(localDensity) { coordinates.size.height.toDp() }
                                }
                            preferedButtonHeight?.let {
                                negativeButtonModifier =
                                    negativeButtonModifier.defaultMinSize(minHeight = it)
                            }

                            var positiveButtonModifier = Modifier
                                .weight(1f)
                                .onGloballyPositioned { coordinates ->
                                    positiveButtonHeight =
                                        with(localDensity) { coordinates.size.height.toDp() }
                                }
                            preferedButtonHeight?.let {
                                positiveButtonModifier =
                                    positiveButtonModifier.defaultMinSize(minHeight = it)
                            }

                            BaseText(text = loremIpsum(20), style = TypeDefinition.bodyMedium)

                            Row(modifier = Modifier.padding(top = 16.dp)) {
                                BaseOutlinedButton(
                                    modifier = negativeButtonModifier,
                                    text = loremIpsum(),
                                    onClick = { dismissClick() }
                                )

                                BaseButton(
                                    modifier = positiveButtonModifier,
                                    text = loremIpsum(5),
                                    onClick = { dismissClick() }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

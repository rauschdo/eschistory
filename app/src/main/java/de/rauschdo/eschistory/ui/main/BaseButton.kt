package de.rauschdo.eschistory.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import de.rauschdo.eschistory.ui.theme.TypeDefinition
import de.rauschdo.eschistory.ui.theme.baseCornerShape
import de.rauschdo.eschistory.ui.theme.defaultButtonHeight
import de.rauschdo.eschistory.ui.theme.purpleLight
import de.rauschdo.eschistory.ui.theme.purpleMain
import de.rauschdo.eschistory.ui.theme.purpleSecondary
import de.rauschdo.eschistory.utility.loremIpsum
import de.rauschdo.eschistory.utility.shadow

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    contentDescription: String? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    content: @Composable () -> Unit = { BaseText(text = text, style = TypeDefinition.labelLarge) }
) {
    var buttonModifier: Modifier = modifier
        .defaultMinSize(
            minHeight = defaultButtonHeight
        )
        .semantics {
            contentDescription?.let {
                this.contentDescription = it
            }
        }

    if (enabled) {
        buttonModifier = buttonModifier.shadow(
            color = purpleLight,
            spread = 1.dp
        )
    }

    Button(
        modifier = buttonModifier,
        contentPadding = contentPadding,
        enabled = enabled,
        shape = baseCornerShape,
        colors = baseButtonColors,
        onClick = onClick
    ) {
        content()
    }
}

val baseButtonColors: ButtonColors
    @Composable get() = ButtonDefaults.buttonColors(
        containerColor = purpleMain,
        contentColor = Color.White,
        disabledContainerColor = purpleSecondary,
        disabledContentColor = Color.LightGray,
    )

@Preview
@Composable
fun BaseButtonPreview(
    @PreviewParameter(BaseButtonPreviewProvider::class) previewData: Boolean
) {
    BaseButton(
        enabled = previewData,
        text = loremIpsum(),
        onClick = {}
    )
}

class BaseButtonPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(
            true,
            false
        )
}
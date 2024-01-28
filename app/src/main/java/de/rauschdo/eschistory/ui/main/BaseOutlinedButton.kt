package de.rauschdo.eschistory.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import de.rauschdo.eschistory.ui.theme.TypeDefinition
import de.rauschdo.eschistory.ui.theme.baseCornerShape
import de.rauschdo.eschistory.ui.theme.defaultButtonHeight
import de.rauschdo.eschistory.ui.theme.defaultLineThickness
import de.rauschdo.eschistory.ui.theme.disabledGrey
import de.rauschdo.eschistory.ui.theme.purpleMain
import de.rauschdo.eschistory.ui.theme.purpleSecondary
import de.rauschdo.eschistory.utility.loremIpsum

val outlinedButtonColors: ButtonColors
    @Composable get() = ButtonDefaults.outlinedButtonColors(
        containerColor = Color.Transparent,
        contentColor = purpleMain
    )

val outlinedButtonDisabledColors: ButtonColors
    @Composable get() = ButtonDefaults.outlinedButtonColors(
        containerColor = purpleSecondary,
        contentColor = Color.LightGray,
    )

val outlinedBorderStroke = BorderStroke(
    width = defaultLineThickness,
    color = Color.LightGray,
)

val outlinedDisabledBorderStroke = BorderStroke(
    width = defaultLineThickness,
    color = disabledGrey,
)

@Composable
fun BaseOutlinedButton(
    modifier: Modifier = Modifier,
    minHeight: Dp = defaultButtonHeight,
    textStyle: TextStyle = TypeDefinition.labelLarge,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = if (enabled) outlinedButtonColors else outlinedButtonDisabledColors,
    border: BorderStroke? = if (enabled) outlinedBorderStroke else outlinedDisabledBorderStroke,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    contentDescription: String? = null,
) {
    val buttonModifier: Modifier = modifier
        .defaultMinSize(
            minHeight = minHeight
        )
        .semantics {
            contentDescription?.let {
                this.contentDescription = it
            }
        }

    OutlinedButton(
        modifier = buttonModifier,
        contentPadding = contentPadding,
        enabled = enabled,
        border = border,
        shape = baseCornerShape,
        colors = colors,
        onClick = onClick
    ) {
        BaseText(text = text, style = textStyle)
    }
}

@Preview(showBackground = true)
@Composable
fun BaseOutlinedButtonPreview() {
    Column {
        BaseOutlinedButton(text = loremIpsum(), onClick = {})
        BaseOutlinedButton(text = loremIpsum(), enabled = false, onClick = {})
    }
}
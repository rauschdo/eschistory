package de.rauschdo.eschistory.ui.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import de.rauschdo.eschistory.ui.theme.TypeDefinition
import de.rauschdo.eschistory.ui.theme.errorRed
import de.rauschdo.eschistory.utility.loremIpsum

@Composable
fun BaseText(
    modifier: Modifier = Modifier,
    text: CharSequence,
    isError: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = TypeDefinition.bodySmall,
    fontSize: TextUnit? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    color: Color? = null,
    onTextLayout: ((linecount: Int) -> Unit)? = null,
    onClick: ((Int) -> Unit)? = null
) {
    if (text is AnnotatedString) {
        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        val pressIndicator = Modifier.pointerInput(onClick) {
            detectTapGestures { pos ->
                layoutResult.value?.let { layoutResult ->
                    onClick?.let { it(layoutResult.getOffsetForPosition(pos)) }
                }
            }
        }

        Text(
            modifier = modifier.then(pressIndicator),
            text = text,
            style = style,
            maxLines = maxLines,
            minLines = minLines,
            fontSize = fontSize ?: style.fontSize,
            fontWeight = fontWeight ?: style.fontWeight,
            textAlign = textAlign,
            overflow = overflow,
            color = if (isError) errorRed else color ?: style.color,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                layoutResult.value = textLayoutResult
                onTextLayout?.let { it(textLayoutResult.lineCount) }
            }
        )
    } else {
        Text(
            modifier = modifier,
            text = text.toString(),
            style = style,
            maxLines = maxLines,
            minLines = minLines,
            fontSize = fontSize ?: style.fontSize,
            fontWeight = fontWeight ?: style.fontWeight,
            textAlign = textAlign,
            overflow = overflow,
            color = if (isError) errorRed else color ?: style.color,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                onTextLayout?.let { it(textLayoutResult.lineCount) }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BaseTextPreview() {
    BaseText(text = loremIpsum())
}
package de.rauschdo.eschistory.utility

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.rauschdo.eschistory.ui.theme.shadowDefault

/**
 * Custom configurable Shadow
 * With light system primarily displaying shadow at the bottom of elevated Views,
 * this [Modifier] serves as alternative to the shadow modifier from UI package by drawing and manipulating rectangle
 * @param color shadow color
 * @param borderRadius basically blur intensity
 * @param cornerRadius rounding of drawn rect
 * @param offsetY
 * @param offsetX
 * @param spread additional increase to drawn rectangle
 * @param useAltImplementation
 */
fun Modifier.shadow(
    color: Color = shadowDefault,
    borderRadius: Dp = 16.dp,
    cornerRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp, // in most cases there should be no need to adjust this
    offsetX: Dp = 0.dp, // ^
    spread: Dp = 0f.dp,
    useAltImplementation: Boolean = false
) = if (!useAltImplementation) {
    shadowAlt()
} else {
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = Color.Transparent.toArgb()

            val shadowColor = color.toArgb()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            frameworkPaint.setShadowLayer(
                /*radius*/borderRadius.toPx(),
                /*dx*/offsetX.toPx(),
                /*dy*/offsetY.toPx(),
                /*color*/shadowColor
            )

            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = cornerRadius.toPx(),
                radiusY = cornerRadius.toPx(),
                paint
            )
        }
    }
}

/**
 * Custom configurable Shadow alternative implementation
 * Doesn't use (just-in-time) composed modifier keyword
 *
 * @param color shadow color
 * @param borderRadius rounding
 * @param blurRadius sharpness in display
 * @param offsetY
 * @param offsetX
 * @param spread additional increase to drawn rectangle
 */
fun Modifier.shadowAlt(
    color: Color = shadowDefault,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 16.dp,
    offsetY: Dp = 7.dp,
    offsetX: Dp = 7.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

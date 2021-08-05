package com.algolia.instantsearch.showcase.compose.filter.range

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.viewinterop.AndroidView
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.showcase.compose.R
import io.apptik.widget.MultiSlider

// the implementation should be replaced later: https://issuetracker.google.com/issues/150221060
@Composable
fun RangeSlider(
    modifier: Modifier = Modifier,
    numberRangeState: NumberRangeState<Int>,
    stepsApart: Int = 1
) {
    val changeListener = changeListener(numberRangeState)
    val factory: (Context) -> MultiSlider = { context ->
        MultiSlider(context).apply {
            stepsThumbsApart = stepsApart
            setOnThumbValueChangeListener(changeListener)
        }
    }
    AndroidView(
        modifier = modifier, // Occupy the max size in the Compose UI tree
        factory = factory,
        update = { slider ->
            numberRangeState.range.let { range ->
                slider.setOnThumbValueChangeListener(null)
                if (range == null) {
                    slider.getThumb(0).unsetThumbValue(numberRangeState.bounds)
                    slider.getThumb(1).unsetThumbValue(numberRangeState.bounds, true)
                } else {
                    slider.getThumb(0).value = range.min
                    slider.getThumb(1).value = range.max
                }
                slider.setOnThumbValueChangeListener(changeListener)
            }

            numberRangeState.bounds.let { bounds ->
                bounds?.let {
                    slider.setMin(it.min, true, false)
                    slider.setMax(it.max, true, false)
                    slider.getThumb(0).value = numberRangeState.range?.min ?: it.min
                    slider.getThumb(1).value = numberRangeState.range?.max ?: it.max
                }
            }
        }
    )
}

private fun changeListener(numberRangeState: NumberRangeState<Int>): (multiSlider: MultiSlider, thumb: MultiSlider.Thumb, thumbIndex: Int, value: Int) -> Unit =
    { multiSlider, _, thumbIndex, value ->
        val valueMin = if (thumbIndex == 0) value else multiSlider.getThumb(0).value
        val valueMax = if (thumbIndex == 1) value else multiSlider.getThumb(1).value
        if (numberRangeState.range?.min != valueMin || numberRangeState.range?.max != valueMax) {
            numberRangeState.onRangeChanged?.invoke(Range(valueMin..valueMax))
        }
    }

private fun MultiSlider.Thumb.unsetThumbValue(
    bounds: Range<Int>?,
    isMax: Boolean = false
) {
    bounds?.let { value = if (isMax) it.max else it.min }
}


fun rangeText(range: Range<Int>?): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        append("Range: ")
        if (range != null) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${range.min}")
            }
            append(" to ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${range.max}")
            }
        } else {
            withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append("?")
            }
        }
        toAnnotatedString()
    }
}

fun boundsText(bounds: Range<Int>?): String {
    return bounds?.let {
        "Bounds: ${it.min} to ${it.max}"
    } ?: "No bounds"
}
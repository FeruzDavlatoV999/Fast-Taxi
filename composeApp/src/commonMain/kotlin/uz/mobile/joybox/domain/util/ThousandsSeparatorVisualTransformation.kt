package uz.mobile.joybox.domain.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ThousandsSeparatorVisualTransformation(val sum:String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedNumber = formatNumberWithSpaces(originalText)
        val finalText = if (originalText.isNotEmpty()) {
            "$formattedNumber $sum"
        } else {
            ""
        }

        val out = AnnotatedString(finalText)

        val offsetMapping = ThousandSeparatorOffsetMapping(
            originalText = originalText,
            transformedText = finalText,
            sum = sum
        )

        return TransformedText(out, offsetMapping)
    }
}

class ThousandSeparatorOffsetMapping(
    private val originalText: String,
    private val transformedText: String,
    private val sum: String,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val formattedUpToOffset = formatNumberWithSpaces(originalText.substring(0, offset))
        var transformedOffset = formattedUpToOffset.length
        if (originalText.isNotEmpty()) {
            transformedOffset += " ${sum}".length
        }

        return transformedOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        val suffixLength = if (originalText.isNotEmpty()) " сум".length else 0
        val offsetWithoutSuffix = (offset - suffixLength).coerceAtLeast(0)

        val formattedTextUpToOffset = transformedText.substring(0, offsetWithoutSuffix)
        val digitsOnly = formattedTextUpToOffset.filter { it.isDigit() }

        return digitsOnly.length.coerceAtMost(originalText.length)
    }

    private fun formatNumberWithSpaces(number: String): String {
        if (number.isEmpty()) return ""

        val reversed = number.reversed()
        val grouped = reversed.chunked(3).joinToString(" ")
        return grouped.reversed()
    }
}

fun formatNumberWithSpaces(number: String): String {
    val reversed = number.reversed()
    val grouped = reversed.chunked(3).joinToString(" ")
    return grouped.reversed()
}
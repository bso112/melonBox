package com.seoulventure.melonbox.util

import androidx.compose.ui.text.AnnotatedString

enum class ClickableStringTag {
    Email
}

fun AnnotatedString.Builder.clickable(
    tag: ClickableStringTag,
    annotation: String,
    block: (annotation: String) -> Unit
) {
    pushStringAnnotation(tag.name, annotation)
    block(annotation)
    pop()
}

fun interface ClickableStringHandler {
    fun handle(tag: ClickableStringTag, text: String)
}

fun AnnotatedString.clickableStringHandler(handler: ClickableStringHandler): (Int) -> Unit {
    return outer@{ offset: Int ->
        getStringAnnotations(offset, offset).forEach { range ->
            val tag =
                runCatching { ClickableStringTag.valueOf(range.tag) }.getOrNull() ?: return@outer
            handler.handle(tag, range.item)
        }

    }
}

package com.seoulventure.melonbox

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.DisposableEffectScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> ImmutableList<T>.newList(mutation: MutableList<T>.() -> Unit): ImmutableList<T> {
    return toMutableList().apply(mutation).toImmutableList()
}


inline fun <reified T> Any.ifIs(block: (T) -> Unit) {
    if (this is T) {
        block(this)
    }
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

val DisposableEffectScope.emptyDisposeResult get() = onDispose { }
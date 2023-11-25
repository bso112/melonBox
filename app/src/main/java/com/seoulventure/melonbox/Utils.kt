package com.seoulventure.melonbox

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> ImmutableList<T>.newList(mutation: MutableList<T>.() -> Unit): ImmutableList<T> {
    return toMutableList().apply(mutation).toImmutableList()
}

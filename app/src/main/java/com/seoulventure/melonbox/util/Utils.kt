package com.seoulventure.melonbox.util

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.seoulventure.melonbox.BuildConfig
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

fun Context.openEmailApp(recipients: Array<String>, titleOfEmail: String) = runCatching {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.setData(Uri.parse("mailto:")) // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, recipients)
    intent.putExtra(Intent.EXTRA_SUBJECT, titleOfEmail)
    startActivity(intent)
}


fun runOnDebug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

fun ClipboardManager.setTextCompat(context: Context, annotatedString: AnnotatedString) {
    setText(annotatedString)
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(context, annotatedString, Toast.LENGTH_SHORT).show()
    }
}
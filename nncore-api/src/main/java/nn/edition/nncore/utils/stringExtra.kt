package nn.edition.nncore.utils

inline fun buildString(str: String, builderAction: StringBuilder.() -> Unit) =
    StringBuilder(str).apply(builderAction).toString()

inline fun String.build(builderAction: StringBuilder.() -> Unit) =
    StringBuilder(this).apply(builderAction).toString()

fun String.builder() = StringBuilder(this)
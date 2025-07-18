package ru.nnedition.nncore.utils

fun <K, V> Map<K, V>.single(): Pair<K, V> {
    assert(this.size == 1)
    return this.map { it.key to it.value }.single()
}
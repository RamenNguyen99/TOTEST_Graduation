package com.example.totest.arch.extensions

internal fun <T> MutableList<T>?.toArrayList(): ArrayList<T> {
    val results = arrayListOf<T>()
    this?.let {
        forEach { item ->
            results.add(item)
        }
    }
    return results
}

internal fun <T> java.util.ArrayList<T>?.toMutableList(): MutableList<T> {
    val results = mutableListOf<T>()
    this?.let {
        forEach { item ->
            results.add(item)
        }
    }
    return results
}

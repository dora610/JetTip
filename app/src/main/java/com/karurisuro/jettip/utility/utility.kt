package com.karurisuro.jettip.utility

fun changeValueWithInRange(newValue: Int, maxValue: Int): Int {
    return 0.coerceAtLeast(maxValue.coerceAtMost(newValue))
}
package com.karurisuro.jettip.utility

fun changeValueWithInRange(newValue: Int, maxValue: Int): Int {
    return 1.coerceAtLeast(maxValue.coerceAtMost(newValue))
}

fun calculateTotalTip( totalBill: String, tipPercentage: Int): Double {
    return if(totalBill.isNotEmpty() && totalBill.toDouble() > 1 ) {
        (totalBill.toDouble() * tipPercentage) / 100
    } else{
        0.0
    }
}

fun calculateTotalPerPerson(totalBill: String, tipPercentage: Int, split: Int): Double {
    val billAmnt = calculateTotalTip(totalBill, tipPercentage) + totalBill.toDouble()
    return billAmnt / split
}
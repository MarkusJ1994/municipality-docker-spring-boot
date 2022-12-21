package com.example.rangola.domain.dto

class NumberConditional(private val colorCode: Int, val matcher: (x: Int) -> Boolean) : Conditional {
    override fun match(x: String): Boolean {
        return matcher(x.toInt())
    }

    override fun getColorCode(): Int {
        return colorCode
    }

}
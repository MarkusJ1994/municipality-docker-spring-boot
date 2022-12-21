package com.example.rangola.domain.dto

class StringConditional(private val colorCode: Int, val matcher: (x: String) -> Boolean) : Conditional {
    override fun match(x: String): Boolean {
        return matcher(x)
    }

    override fun getColorCode(): Int {
        return colorCode
    }
}
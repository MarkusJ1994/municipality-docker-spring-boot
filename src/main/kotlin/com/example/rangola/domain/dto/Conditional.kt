package com.example.rangola.domain.dto

interface Conditional {

    fun match(x: String): Boolean

    fun getColorCode(): Int

}
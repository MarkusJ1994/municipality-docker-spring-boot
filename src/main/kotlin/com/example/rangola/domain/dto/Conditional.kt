package com.example.rangola.domain.dto

class Conditional(val code: Int, val matcher: (x: String) -> Boolean) {
}
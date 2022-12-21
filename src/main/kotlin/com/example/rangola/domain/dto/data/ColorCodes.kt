package com.example.rangola.domain.dto.data

enum class ColorCodes(val hex: String) {
    GREEN("#38761d"),
    RED("#ff0000"),
    YELLOW("#ffff00"),
    ORANGE("#f4b083"),
    BEIGE("#fef2cb"),
    LIGHTBLUE("#deeaf6"),
    BLUE("#9cc2e5"),
    GREY("#b7b7b7")
}

val colorCodes: Map<Int, ColorCodes> = mapOf(
    1 to ColorCodes.GREEN,
    2 to ColorCodes.RED,
    3 to ColorCodes.YELLOW,
    4 to ColorCodes.ORANGE,
    5 to ColorCodes.BEIGE,
    6 to ColorCodes.LIGHTBLUE,
    7 to ColorCodes.BLUE,
    8 to ColorCodes.GREY
)

package com.example.rangola.service.excel

import com.example.rangola.domain.dto.Conditional
import com.example.rangola.domain.dto.NumberConditional
import com.example.rangola.domain.dto.StringConditional

class ConditionalParser(valueToColorCol: Int) {

    private enum class Operator(val op: String) {
        EQUAL("eq"),
        GREATER("gt"),
        LESSER("lt"),
        GREATER_OR_EQUAL("gt_eq"),
        LESSER_OR_EQUAL("lt_eq")
    }

    private enum class Type {
        NUMBER, STRING
    }

    /**
     * Examples:
     * | 1 | 5 | = | => value = 5 maps to color code 1
     * | 2 | 1 | < | 6 | => 1 < value < 6 maps to color code 2
     * | 3 | 6 | >= | => value >= 6 maps to color code 3
     */
    private val colorCodeCol: Int = valueToColorCol
    private val typeCol: Int = valueToColorCol + 1
    private val firstConditionalCol: Int = valueToColorCol + 2
    private val operatorCol: Int = valueToColorCol + 3
    private val secondConditionalCol: Int = valueToColorCol + 4

    fun parseConditionals(sheetData: Map<Int, List<String>>): List<Conditional> {
        return sheetData.filter { rowIsWithinBounds(it) }.map { constructConditional(it) }
    }

    private fun rowIsWithinBounds(entry: Map.Entry<Int, List<String>>): Boolean {
        return entry.value[colorCodeCol].isNotBlank()
    }

    private fun constructConditional(entry: Map.Entry<Int, List<String>>): Conditional {
        val colorCode = entry.value[colorCodeCol].toDouble().toInt()
        val typeCol: Type = Type.valueOf(entry.value[typeCol])

        return if (typeCol == Type.NUMBER) NumberConditional(
            colorCode, numberMatcher(
                entry.value[operatorCol],
                entry.value[firstConditionalCol].toInt(),
                if (entry.value.size == secondConditionalCol + 1) entry.value[secondConditionalCol].toInt() else null
            )
        )
        else StringConditional(colorCode, stringMatcher(entry.value[operatorCol], entry.value[firstConditionalCol]))

    }

    private fun numberMatcher(operator: String, firstValue: Int, secondValue: Int?): (x: Int) -> Boolean {
        return when (operator) {
            Operator.EQUAL.op -> { x -> x == firstValue }
            Operator.GREATER.op -> { x -> if (secondValue != null) x > firstValue && x < secondValue else x > firstValue }
            Operator.GREATER_OR_EQUAL.op -> { x -> if (secondValue != null) x >= firstValue && x <= secondValue else x >= firstValue }
            Operator.LESSER.op -> { x -> if (secondValue != null) x < firstValue && x > secondValue else x < firstValue }
            Operator.LESSER_OR_EQUAL.op -> { x -> if (secondValue != null) x <= firstValue && x >= secondValue else x <= firstValue }
            else -> {
                throw IllegalStateException("Unknown operator in mapping columns")
            }
        }
    }

    private fun stringMatcher(operator: String, firstValue: String): (x: String) -> Boolean {
        return when (operator) {
            Operator.EQUAL.op -> { x -> x == firstValue }
            else -> {
                throw IllegalStateException("Unknown operator in mapping columns")
            }
        }
    }

}
package com.example.rangola.service.excel

import com.example.rangola.domain.dto.Conditional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConditionalParserTest {

    private val stringTestData = mapOf(
        1 to listOf("1", "STRING", "ett", "eq"),
        2 to listOf("2", "STRING", "två", "eq"),
        3 to listOf("3", "STRING", "tre", "eq")
    )

    private val numberTestData = mapOf(
        1 to listOf("1", "NUMBER", "1", "eq"),
        2 to listOf("2", "NUMBER", "3", "gt"),
        3 to listOf("3", "NUMBER", "3", "lt_eq"),
        4 to listOf("5", "NUMBER", "5", "lt", "3")
    )

    @Test
    fun testStringParser() {
        val conditionalParser = ConditionalParser(0)
        val parseConditionals: List<Conditional> = conditionalParser.parseConditionals(stringTestData)

        assertTrue(parseConditionals[0].match("ett"))
        assertFalse(parseConditionals[0].match("1"))

        val badStringTestData = mapOf(
            1 to listOf("1", "STRING", "ett", "gt"),
        )

        assertThrows(IllegalStateException::class.java) { conditionalParser.parseConditionals(badStringTestData) }
    }

    @Test
    fun testNumberParser() {
        val conditionalParser = ConditionalParser(0)
        val parseConditionals: List<Conditional> = conditionalParser.parseConditionals(numberTestData)

        assertTrue(parseConditionals[0].match("1"))
        assertFalse(parseConditionals[0].match("2"))

        assertTrue(parseConditionals[1].match("4"))
        assertFalse(parseConditionals[1].match("3"))

        assertTrue(parseConditionals[2].match("3"))
        assertFalse(parseConditionals[2].match("4"))

        assertTrue(parseConditionals[3].match("4"))
        assertFalse(parseConditionals[3].match("1"))

        val badNumberTestData = mapOf(
            1 to listOf("1", "NUMBER", "1", "gttt"),
        )

        assertThrows(IllegalStateException::class.java) { conditionalParser.parseConditionals(badNumberTestData) }
    }

}
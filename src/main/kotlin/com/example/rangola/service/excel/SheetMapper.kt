package com.example.rangola.service.excel

import com.example.rangola.domain.dto.Conditional
import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.domain.dto.data.municipalityNameToCodeMap

class SheetMapper(
    private val municipalityColumn: Int,
    private val valueColumn: Int,
    private val firstRow: Int,
    private val lastRow: Int,
    private val sheetData: Map<Int, List<String>>,
    valueToColorColumn: Int? = null
) {

    private val conditionalList: List<Conditional>? =
        if (valueToColorColumn != null) ConditionalParser(valueToColorColumn).parseConditionals(sheetData) else null

    fun mapSheet(): List<RowEntry> {
        return sheetData.filter { rowIsWithinBounds(it.key) }.map { constructMappedEntry(it) }
    }

    private fun rowIsWithinBounds(row: Int): Boolean {
        return row in firstRow..lastRow
    }

    private fun constructMappedEntry(entry: Map.Entry<Int, List<String>>): RowEntry {
        return RowEntry(
            getMunicipalityName(entry.value),
            getMunicipalityCode(entry.key, entry.value),
            getRowValue(entry.value),
            getRowValueColorCode(entry.value)
        )
    }

    private fun getMunicipalityName(columns: List<String>): String {
        return columns[municipalityColumn]
    }

    private fun getMunicipalityCode(row: Int, columns: List<String>): String {
        val municipalityCode: String? = municipalityNameToCodeMap[getMunicipalityName(columns)]
        return municipalityCode ?: "$row-null"
    }

    private fun getRowValue(columns: List<String>): String {
        return columns[valueColumn]
    }

    private fun getRowValueColorCode(columns: List<String>): Int {
        if (conditionalList != null) {
            for (conditional in conditionalList) {
                if (conditional.match(getRowValue(columns))) {
                    return conditional.getColorCode()
                }
            }
        } else {
            return try {
                getRowValue(columns).toDouble().toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }
        return 0
    }

}
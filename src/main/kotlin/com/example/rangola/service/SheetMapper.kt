package com.example.rangola.service

import com.example.rangola.data.municipalityNameToCodeMap
import com.example.rangola.dto.Conditional
import com.example.rangola.dto.RowEntry

class SheetMapper(
    private val municipalityColumn: Int,
    private val valueColumn: Int,
    private val firstRow: Int,
    private val lastRow: Int,
    val conditionalList: List<Conditional>
) {

    fun mapSheet(sheetData: Map<Int, List<String>>): List<RowEntry> {
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
        val municipalityCode: String? = municipalityNameToCodeMap()[getMunicipalityName(columns)]
        return municipalityCode ?: "$row-null"
    }

    private fun getRowValue(columns: List<String>): String {
        return columns[valueColumn]
    }

    private fun getRowValueColorCode(columns: List<String>): Int {
        for (conditional in conditionalList) {
            if (conditional.matcher(getRowValue(columns))) {
                return conditional.code
            }
        }
        return 0
    }

}
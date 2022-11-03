package com.example.rangola.service

import com.example.rangola.dto.RowEntry

class OutputWriter {

    fun writeOutput(rowEntries: List<RowEntry>): String {
        val output: String = rowEntries.map { row: RowEntry ->
            row.colorCode.toString()
        }.reduce { acc: String, rowColorCode: String -> if (acc.isEmpty()) rowColorCode else "$acc, $rowColorCode" }
        return output
    }

}
package com.example.rangola.service.excel

import com.example.rangola.domain.dto.Order
import com.example.rangola.domain.dto.RowEntry
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class ExcelParser(
    private val municipalityCol: Int,
    private val valueCol: Int,
    private val valueToColorCol: Int? = null,
) {

    fun readExcelFile(order: Order): List<RowEntry> {
        try {
            val file = FileInputStream(order.path)
            return readExcelFile(file)

        } catch (e: IOException) {
            println("Could not parse excel file")
            return arrayListOf()
        }
    }

    fun readExcelFile(file: InputStream): List<RowEntry> {
        return try {
            val workbook: Workbook = XSSFWorkbook(file)

            val sheet = workbook.getSheetAt(0)

            val parsedSheet = readSheet(
                sheet
            )

            SheetMapper(
                municipalityCol, valueCol, 0, 289, parsedSheet, valueToColorCol
            ).mapSheet(
            )

        } catch (e: IOException) {
            println("Could not parse excel file")
            arrayListOf()
        }


    }

    private fun readSheet(sheet: Sheet): Map<Int, MutableList<String>> {
        val data: MutableMap<Int, MutableList<String>> = HashMap()
        for ((i, row) in sheet.withIndex()) {
            val rowData: MutableList<String> = ArrayList()
            for (cell in row) {
                when (cell.cellType) {
                    CellType.STRING -> rowData.add(cell.richStringCellValue.string)
                    CellType.NUMERIC -> if (DateUtil.isCellDateFormatted(cell)) {
                        rowData.add(cell.dateCellValue.toString() + "")
                    } else {
                        rowData.add(cell.numericCellValue.toString() + "")
                    }

                    CellType.BOOLEAN -> rowData.add(cell.booleanCellValue.toString() + "")
                    CellType.FORMULA -> rowData.add(cell.cellFormula + "")
                    else -> rowData.add(" ")
                }
            }
            data[i] = rowData
        }
        return data
    }


}
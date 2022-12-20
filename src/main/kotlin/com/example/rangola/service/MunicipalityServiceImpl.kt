package com.example.rangola.service

import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.svg.SvgAreaFilter
import com.example.rangola.service.svg.SvgFiller
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MunicipalityServiceImpl(val svgFiller: SvgFiller, val svgAreaFilter: SvgAreaFilter) : MunicipalityService {

    override fun getColoredMapFull(file: MultipartFile, municipalityCol: String, valueCol: String): String {
        //Parse document
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        val document =
            svgFiller.fillDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())

        val filePath = "output/sweden.svg"

        svgFiller.generateOutput(document, filePath)
        return filePath
    }

    override fun getColoredMapStockholm(file: MultipartFile, municipalityCol: String, valueCol: String): String {
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        val document =
            svgFiller.fillDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())

        val filePath = "output/stockholm.svg"

        svgFiller.generateOutput(
            svgAreaFilter.stockholm(
                document
            ),
            filePath
        )
        return filePath
    }
}
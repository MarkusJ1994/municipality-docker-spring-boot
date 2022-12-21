package com.example.rangola.service

import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.svg.SvgAreaFilter
import com.example.rangola.service.svg.SvgService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MunicipalityServiceImpl(val svgService: SvgService, val svgAreaFilter: SvgAreaFilter) : MunicipalityService {

    override fun coloredMapSweden(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String {
        //Parse document
        val rowEntries: List<RowEntry> =
            ExcelParser(
                municipalityCol.toInt(),
                valueCol.toInt(),
                valueToColorCol?.toInt()
            ).readExcelFile(file.inputStream)
        val document =
            svgService.colorDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())

        val filePath = "output/sweden.svg"

        svgService.generateOutput(document, filePath)
        return filePath
    }

    override fun coloredMapStockholm(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String {
        val rowEntries: List<RowEntry> =
            ExcelParser(
                municipalityCol.toInt(),
                valueCol.toInt(),
                valueToColorCol?.toInt()
            ).readExcelFile(file.inputStream)
        val document =
            svgService.colorDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())

        val filePath = "output/stockholm.svg"

        svgService.generateOutput(
            svgAreaFilter.stockholm(
                document
            ),
            filePath
        )
        return filePath
    }

    override fun coloredMapGothenburg(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String {
        val rowEntries: List<RowEntry> =
            ExcelParser(
                municipalityCol.toInt(),
                valueCol.toInt(),
                valueToColorCol?.toInt()
            ).readExcelFile(file.inputStream)
        val document =
            svgService.colorDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())

        val filePath = "output/gothenburg.svg"

        svgService.generateOutput(
            svgAreaFilter.gothenburg(
                document
            ),
            filePath
        )
        return filePath
    }

}
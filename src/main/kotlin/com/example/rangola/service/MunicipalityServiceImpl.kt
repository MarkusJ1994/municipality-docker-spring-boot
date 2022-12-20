package com.example.rangola.service

import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.svg.SvgAreaFilter
import com.example.rangola.service.svg.SvgFiller
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

@Service
class MunicipalityServiceImpl(val svgFiller: SvgFiller, val svgAreaFilter: SvgAreaFilter) : MunicipalityService {

    override fun getColoredMapFull(file: MultipartFile, municipalityCol: String, valueCol: String): InputStream {
        //Parse document
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        val document =
            svgFiller.fillDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())
        svgFiller.generateOutput(document)
        return File("output/output.svg").inputStream()
    }

    override fun getColoredMapStockholm(file: MultipartFile, municipalityCol: String, valueCol: String): InputStream {
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        val document =
            svgFiller.fillDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
                .toMap())
        println("Document filled")
        svgFiller.generateOutput(
            svgAreaFilter.stockholm(
                document
            )
        )
        return File("output/output.svg").inputStream()
    }
}
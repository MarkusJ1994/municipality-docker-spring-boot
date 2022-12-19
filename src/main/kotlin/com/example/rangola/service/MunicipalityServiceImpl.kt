package com.example.rangola.service

import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.svg.SvgFiller
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

@Service
class MunicipalityServiceImpl : MunicipalityService {

    override fun getColoredMap(file: MultipartFile, municipalityCol: String, valueCol: String): InputStream {
        //Parse document
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        SvgFiller().parseSvgDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
            .toMap())
        return File("output/output.svg").inputStream()
    }
}
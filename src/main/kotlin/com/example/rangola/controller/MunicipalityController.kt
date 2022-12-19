package com.example.rangola.controller

import com.example.rangola.domain.dto.Order
import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.ExcelParser
import com.example.rangola.service.OutputWriter
import com.example.rangola.service.SvgFiller
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL
import java.util.*


@RestController
class MunicipalityController {

    @GetMapping("/")
    fun index(): String {
        return "Greetings from Rangola!";
    }

    @PostMapping("/order")
    @ResponseBody
    fun order(order: Order): ResponseEntity<String> {
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=script.jsx")
        val rowEntries: List<RowEntry> = ExcelParser(0, 2).readExcelFile(order)
        return ResponseEntity.ok().headers(header).contentType(MediaType.TEXT_PLAIN)
            .body(OutputWriter().writeOutput(rowEntries))
    }

    @PostMapping("/order/map", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMap(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String
    ): ResponseEntity<InputStreamResource> {
        val resource: URL = javaClass.classLoader.getResource("SWE-Map_Kommuner2007.svg")
        val uri = resource.toURI()
        //Parse document
        val rowEntries: List<RowEntry> =
            ExcelParser(municipalityCol.toInt(), valueCol.toInt()).readExcelFile(file.inputStream)
        SvgFiller(uri).parseSvgDocument(rowEntries.map { rowEntry -> rowEntry.municipalityCode to rowEntry.colorCode }
            .toMap())
        val output = File("output/output.svg").inputStream()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(InputStreamResource(output))
    }

}
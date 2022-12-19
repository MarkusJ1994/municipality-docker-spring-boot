package com.example.rangola.controller

import com.example.rangola.domain.dto.Order
import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.MunicipalityService
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.excel.OutputWriter
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
class MunicipalityController(val municipalityService: MunicipalityService) {

    @PostMapping("/order")
    @ResponseBody
    fun order(order: Order): ResponseEntity<String> {
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=script.jsx")
        val rowEntries: List<RowEntry> = ExcelParser(0, 2).readExcelFile(order)
        return ResponseEntity.ok().headers(header).contentType(MediaType.TEXT_PLAIN)
            .body(OutputWriter().writeOutput(rowEntries))
    }

    @PostMapping("/order/map/preview", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMapPreview(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String
    ): ResponseEntity<InputStreamResource> {
        val output = municipalityService.getColoredMap(file, municipalityCol, valueCol)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(InputStreamResource(output))
    }

    @PostMapping("/order/map", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMap(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String
    ): ResponseEntity<InputStreamResource> {
        val output = municipalityService.getColoredMap(file, municipalityCol, valueCol)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.svg")
        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_XML)
            .body(InputStreamResource(output))
    }

}
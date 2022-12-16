package com.example.rangola.controller

import com.example.rangola.dto.Order
import com.example.rangola.dto.RowEntry
import com.example.rangola.service.ExcelParser
import com.example.rangola.service.OutputWriter
import com.example.rangola.service.SvgFiller
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.awt.Point
import java.net.URL
import java.util.*

@RestController
class MunicipalityController {

    @GetMapping("/")
    fun index(): String {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/order")
    @ResponseBody
    fun order(order: Order): ResponseEntity<String> {
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=script.jsx")
        val rowEntries: List<RowEntry> = ExcelParser().readExcelFile(order)
        return ResponseEntity.ok().headers(header).contentType(MediaType.TEXT_PLAIN)
            .body(OutputWriter().writeOutput(rowEntries))
    }

    @GetMapping("/svg")
    fun listSVGIds(): ResponseEntity<String> {
        val resource: URL = javaClass.classLoader.getResource("SWE-Map_Kommuner2007.svg")
        val uri = resource.toURI()
        val ids = SvgFiller().listIds(uri)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(ids)
    }

}
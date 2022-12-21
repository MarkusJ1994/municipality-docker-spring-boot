package com.example.rangola.controller

import com.example.rangola.domain.dto.Order
import com.example.rangola.domain.dto.RowEntry
import com.example.rangola.service.MunicipalityService
import com.example.rangola.service.excel.ExcelParser
import com.example.rangola.service.excel.OutputWriter
import org.apache.commons.compress.utils.IOUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


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

    @PostMapping("/order/map/sweden", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMap(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String,
        @RequestParam("valueToColorCol") valueToColorCol: String?
    ): ResponseEntity<InputStreamResource> {
        val filePath = municipalityService.coloredMapSweden(file, municipalityCol, valueCol, valueToColorCol)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sweden.svg")
        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_XML)
            .body(InputStreamResource(File(filePath).inputStream()))
    }

    @PostMapping("/order/map/dalarna", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMapDalarna(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String,
        @RequestParam("valueToColorCol") valueToColorCol: String?
    ): ResponseEntity<InputStreamResource> {
        val filePath = municipalityService.coloredMapDalarna(file, municipalityCol, valueCol, valueToColorCol)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dalarna.svg")
        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_XML)
            .body(InputStreamResource(File(filePath).inputStream()))
    }

    @PostMapping("/order/map/stockholm", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMapStockholm(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String,
        @RequestParam("valueToColorCol") valueToColorCol: String?
    ): ResponseEntity<InputStreamResource> {
        val filePath = municipalityService.coloredMapStockholm(file, municipalityCol, valueCol, valueToColorCol)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stockholm.svg")
        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_XML)
            .body(InputStreamResource(File(filePath).inputStream()))
    }

    @PostMapping("/order/map/gothenburg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMapGothenburg(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String,
        @RequestParam("valueToColorCol") valueToColorCol: String?
    ): ResponseEntity<InputStreamResource> {
        val filePath = municipalityService.coloredMapGothenburg(file, municipalityCol, valueCol, valueToColorCol)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=gothenburg.svg")
        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_XML)
            .body(InputStreamResource(File(filePath).inputStream()))
    }

    @PostMapping("/order/map", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun orderColoredMapAll(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("municipalityCol") municipalityCol: String,
        @RequestParam("valueCol") valueCol: String,
        @RequestParam("valueToColorCol") valueToColorCol: String?
    ): ResponseEntity<StreamingResponseBody> {
        val sweden = municipalityService.coloredMapSweden(file, municipalityCol, valueCol, valueToColorCol)
        val sthlm = municipalityService.coloredMapStockholm(file, municipalityCol, valueCol, valueToColorCol)
        val gothenburg = municipalityService.coloredMapGothenburg(file, municipalityCol, valueCol, valueToColorCol)

        val files = listOf(
            File(sweden), File(sthlm), File(gothenburg)
        )

        return ResponseEntity.ok()
            .contentType(MediaType("application", "zip"))
            .header("Content-Disposition", "attachment; filename=map.zip")
            .body(StreamingResponseBody { out ->
                val zipOutputStream = ZipOutputStream(out)

                // package files
                for (mapFile in files) {
                    //new zip entry and copying inputstream with mapFile to zipOutputStream, after all closing streams
                    zipOutputStream.putNextEntry(ZipEntry(mapFile.name))
                    val fileInputStream = FileInputStream(mapFile)
                    IOUtils.copy(fileInputStream, zipOutputStream)
                    fileInputStream.close()
                    zipOutputStream.closeEntry()
                }
                zipOutputStream.close()
            })
    }

}
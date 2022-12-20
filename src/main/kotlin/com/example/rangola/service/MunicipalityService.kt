package com.example.rangola.service

import org.springframework.web.multipart.MultipartFile

interface MunicipalityService {

    fun getColoredMapFull(file: MultipartFile, municipalityCol: String, valueCol: String): String

    fun getColoredMapStockholm(file: MultipartFile, municipalityCol: String, valueCol: String): String

}
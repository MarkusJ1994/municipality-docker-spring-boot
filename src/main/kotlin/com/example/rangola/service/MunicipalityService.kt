package com.example.rangola.service

import org.springframework.web.multipart.MultipartFile

interface MunicipalityService {

    fun coloredMapSweden(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String

    fun coloredMapStockholm(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String

    fun coloredMapGothenburg(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String

    fun coloredMapDalarna(
        file: MultipartFile,
        municipalityCol: String,
        valueCol: String,
        valueToColorCol: String?
    ): String

}
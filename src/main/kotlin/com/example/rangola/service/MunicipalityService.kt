package com.example.rangola.service

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

interface MunicipalityService {

    fun getColoredMap(file: MultipartFile, municipalityCol: String, valueCol: String): InputStream

}
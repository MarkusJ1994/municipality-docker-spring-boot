package com.example.rangola.service.svg

import org.w3c.dom.Document

interface SvgService {

    fun colorDocument(municipalityToColorCode: Map<String, Int>): Document

    fun generateOutput(document: Document, filePath: String)

}
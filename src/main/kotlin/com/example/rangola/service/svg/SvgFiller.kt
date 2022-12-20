package com.example.rangola.service.svg

import org.w3c.dom.Document

interface SvgFiller {

    fun fillDocument(municipalityToColorCode: Map<String, Int>): Document

    fun generateOutput(document: Document, filePath: String)

}
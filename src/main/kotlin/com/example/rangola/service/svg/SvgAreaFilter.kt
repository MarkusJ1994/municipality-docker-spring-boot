package com.example.rangola.service.svg

import org.w3c.dom.Document

interface SvgAreaFilter {

    fun stockholm(document: Document): Document

    fun gothenburg(document: Document): Document

}
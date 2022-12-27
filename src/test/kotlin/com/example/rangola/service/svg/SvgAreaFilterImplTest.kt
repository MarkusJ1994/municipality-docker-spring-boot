package com.example.rangola.service.svg

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.w3c.dom.Element
import org.w3c.dom.Node

class SvgAreaFilterImplTest {

    val svgAreaFilter: SvgAreaFilterImpl = SvgAreaFilterImpl()

    @Test
    fun stockholm() {
        val stockholm = svgAreaFilter.stockholm(SvgServiceImpl().initDocument())

        val nodeList = stockholm.getElementsByTagName("*")

        var mCount = 0

        var i = 0
        while (i < nodeList.length) {
            val item: Element = nodeList.item(i) as Element
            if (item.nodeType == Node.ELEMENT_NODE && (item.nodeName == "polygon" || item.nodeName == "g")) {
                val nodeId = item.attributes.getNamedItem("id").nodeValue
                assertTrue(svgAreaFilter.stockholmCodes.containsKey(nodeId))
                mCount++
                if (item.nodeName == "g") {
                    //Skip group children
                    i += item.getElementsByTagName("*").length
                }
            }
            i++
        }

        assertEquals(svgAreaFilter.stockholmCodes.size, mCount)
    }

    @Test
    fun gothenburg() {
        val gothenburg = svgAreaFilter.gothenburg(SvgServiceImpl().initDocument())

        val nodeList = gothenburg.getElementsByTagName("*")

        var mCount = 0

        var i = 0
        while (i < nodeList.length) {
            val item: Element = nodeList.item(i) as Element
            if (item.nodeType == Node.ELEMENT_NODE && (item.nodeName == "polygon" || item.nodeName == "g")) {
                val nodeId = item.attributes.getNamedItem("id").nodeValue
                assertTrue(svgAreaFilter.gothenburgCodes.containsKey(nodeId))
                mCount++
                if (item.nodeName == "g") {
                    //Skip group children
                    i += item.getElementsByTagName("*").length
                }
            }
            i++
        }

        assertEquals(svgAreaFilter.gothenburgCodes.size, mCount)
    }

    @Test
    fun dalarna() {
        val stockholm = svgAreaFilter.dalarna(SvgServiceImpl().initDocument())

        val nodeList = stockholm.getElementsByTagName("*")

        var mCount = 0

        var i = 0
        while (i < nodeList.length) {
            val item: Element = nodeList.item(i) as Element
            if (item.nodeType == Node.ELEMENT_NODE && (item.nodeName == "polygon" || item.nodeName == "g")) {
                val nodeId = item.attributes.getNamedItem("id").nodeValue
                assertTrue(svgAreaFilter.dalarnaCodes.containsKey(nodeId))
                mCount++
                if (item.nodeName == "g") {
                    //Skip group children
                    i += item.getElementsByTagName("*").length
                }
            }
            i++
        }

        assertEquals(svgAreaFilter.dalarnaCodes.size, mCount)
    }
}
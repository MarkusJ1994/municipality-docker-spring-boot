package com.example.rangola.service.svg

import com.example.rangola.domain.dto.data.colorCodes
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.net.URI
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class SvgFiller {

    private val document: Document

    init {
        val resource: URL? = javaClass.classLoader.getResource("SWE-Map_Kommuner2007.svg")
        if (resource != null) {
            val uri = resource.toURI()
            document = initDocument(uri)
        } else {
            throw IllegalStateException("Template svg map not found")
        }
    }

    private fun initDocument(uri: URI): Document {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        return builder.parse(uri.toString())
    }

    fun parseSvgDocument(municipalityToColorCode: Map<String, Int>) {
        var municipalities = 0
        val nodeList = document.getElementsByTagName("*")
        for (i in 0 until nodeList.length) {
            val node: Element = nodeList.item(i) as Element
            if (node.nodeType == Node.ELEMENT_NODE && (node.nodeName == "polygon" || node.nodeName == "g")) {
                val nodeId = node.attributes.getNamedItem("id").nodeValue
                if (municipalityToColorCode.containsKey(nodeId)) {
                    if (node.nodeName == "g") { // Is a group of polygons, need to iterate
                        val childNodes = node.getElementsByTagName("*")
                        for (i in 0 until childNodes.length) {
                            val childNode = childNodes.item(i)
//                            println(node.nodeName)
//                            println(node.attributes.getNamedItem("id").nodeValue)
//                            println(node.attributes.getNamedItem("style"))
//                            println(childNode.nodeName)
//                            println(childNode.attributes.getNamedItem("id").nodeValue)
//                            println(childNode.attributes.getNamedItem("style"))
//                            println()
                            parsePolygon(
                                childNode,
                                nodeId,
                                municipalityToColorCode
                            )
                        }
                    }
                    parsePolygon(node, nodeId, municipalityToColorCode)
                    municipalities++
                }
            }
        }

        println("Municipalities parsed: $municipalities")

        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        val output = StreamResult(File("output/output.svg"))
        val input: Source = DOMSource(document)
        transformer.transform(input, output)
    }

    private fun parsePolygon(node: Node, nodeId: String, municipalityToColorCode: Map<String, Int>) {
        val style: Node? = node.attributes.getNamedItem("style")
        val colorCode: String? = colorCodes[municipalityToColorCode[nodeId]]
        if (style != null && colorCode != null) {
            style.textContent = style.textContent.replace("#ccc", colorCode)
        }
    }

}
package com.example.rangola.service.svg

import com.example.rangola.domain.dto.data.colorCodes
import org.springframework.stereotype.Component
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Component
class SvgServiceImpl : SvgService {

    private fun initDocument(): Document {
        val resource: URL? = javaClass.classLoader.getResource("SWE-Map_Kommuner2007.svg")
        if (resource != null) {
            val uri = resource.toURI()
            val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val builder: DocumentBuilder = factory.newDocumentBuilder()
            return builder.parse(uri.toString())
        } else {
            throw IllegalStateException("Template svg map not found")
        }
    }

    override fun generateOutput(document: Document, filePath: String) {
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        val output = StreamResult(File(filePath))
        val input: Source = DOMSource(document)
        transformer.transform(input, output)
    }

    override fun colorDocument(municipalityToColorCode: Map<String, Int>): Document {
        val document = initDocument()
        var municipalities = 0
        val nodeList = document.getElementsByTagName("*")
        for (i in 0 until nodeList.length) {
            val node: Element = nodeList.item(i) as Element
            if (node.nodeType == Node.ELEMENT_NODE && (node.nodeName == "polygon" || node.nodeName == "g")) {
                val nodeId = node.attributes.getNamedItem("id").nodeValue
                if (municipalityToColorCode.containsKey(nodeId)) {
                    if (node.nodeName == "g") { // Is a group of polygons, need to iterate
                        val childNodes = node.getElementsByTagName("*")
                        for (j in 0 until childNodes.length) {
                            val childNode = childNodes.item(j)
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

        return document
    }

    private fun parsePolygon(node: Node, nodeId: String, municipalityToColorCode: Map<String, Int>) {
        val style: Node? = node.attributes.getNamedItem("style")
        val colorCode: String? = colorCodes[municipalityToColorCode[nodeId]]?.hex
        if (style != null && colorCode != null) {
            style.textContent = style.textContent.replace("#ccc", colorCode)
        }
    }

}
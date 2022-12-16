package com.example.rangola.service

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.awt.Point
import java.io.File
import java.net.URI
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory


class SvgFiller {

    private fun initDocument(uri: URI): Document {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        return builder.parse(uri.toString())
    }

    fun printSvg(uri: URI): Array<Point?> {
        val document: Document = initDocument(uri)
        val xpath: XPath = XPathFactory.newInstance().newXPath()
        val expr: XPathExpression = xpath.compile("//polygon[@id='2417']/@style")
        val pointsAttr = (expr.evaluate(document, XPathConstants.STRING) as String).split("\\p{Space}".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val points: Array<Point?> = arrayOfNulls<Point>(pointsAttr.size)
        for (i in pointsAttr.indices) {
            val coordinates = pointsAttr[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            println(coordinates)
            points[i] = Point(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]))
        }
        return points
    }

    fun listIds(uri: URI): String {
        val document: Document = initDocument(uri)
        val xpath: XPath = XPathFactory.newInstance().newXPath()
        val expr: XPathExpression = xpath.compile("//polygon/@id")
        val idAttr = (expr.evaluate(document, XPathConstants.STRING) as String)

        parseSvgDocument(uri)

        return idAttr
    }

    fun parseSvgDocument(uri: URI) {
        val document: Document = initDocument(uri)

        val nodeList = document.getElementsByTagName("*")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE && node.nodeName == "polygon") {
                // do something with the current element
                println(node.nodeName)
                for (j in 0 until node.attributes.length) {
                    val item = node.attributes.item(j)
                    println(item.nodeValue)
                    println(item.nodeName)
                    item.textContent = item.textContent.replace("#ccc", "#ccf")
                    println(item.nodeValue)
                    println()
                }
            }
        }

        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        val output = StreamResult(File("output/output.svg"))
        val input: Source = DOMSource(document)
        transformer.transform(input, output)
    }

}
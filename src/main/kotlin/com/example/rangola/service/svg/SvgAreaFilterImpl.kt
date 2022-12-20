package com.example.rangola.service.svg

import org.springframework.stereotype.Component
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node


@Component
class SvgAreaFilterImpl : SvgAreaFilter {

    val stockholmViewBox = "150 420 50 80"
    val stockholmCodes: Map<String, String> = mapOf(
        "0114" to "Upplands Väsby",
        "0115" to "Vallentuna",
        "0117" to "Österåker",
        "0120" to "Värmdö",
        "0123" to "Järfälla",
        "0125" to "Ekerö",
        "0126" to "Huddinge",
        "0127" to "Botkyrka",
        "0128" to "Salem",
        "0136" to "Haninge",
        "0138" to "Tyresö",
        "0139" to "Upplands-Bro",
        "0140" to "Nykvarn",
        "0160" to "Täby",
        "0162" to "Danderyd",
        "0163" to "Sollentuna",
        "0180" to "Stockholm",
        "0181" to "Södertälje",
        "0182" to "Nacka",
        "0183" to "Sundbyberg",
        "0184" to "Solna",
        "0186" to "Lidingö",
        "0187" to "Vaxholm",
        "0188" to "Norrtälje",
        "0191" to "Sigtuna",
        "0192" to "Nynäshamn",
    )

    override fun stockholm(document: Document): Document {
        val nodeList = document.getElementsByTagName("*")

        var toRemove = false

        //Hard coded towards the sweden map
        var currNode = nodeList.item(1)
        while (currNode != null) {
            if (currNode.nodeType == Node.ELEMENT_NODE && (currNode.nodeName == "polygon" || currNode.nodeName == "g")) {
                val nodeId = currNode.attributes.getNamedItem("id").nodeValue
                toRemove = !stockholmCodes.containsKey(nodeId)
            }
            if (toRemove) {
                val nodeToRemove = currNode
                currNode = currNode.nextSibling
                nodeToRemove.parentNode.removeChild(nodeToRemove)
                toRemove = false
            } else {
                currNode = currNode.nextSibling
            }
        }

        applyViewBox(document, stockholmViewBox)

        return document
    }

//    fun gothenburg(): Document {
//        return null
//    }
//
//    fun dalarna(): Document {
//        return null
//    }

    fun applyViewBox(document: Document, viewBox: String) {
        val svgTag = document.getElementsByTagName("svg").item(0) as Element
        svgTag.setAttribute("viewBox", viewBox)
    }

}
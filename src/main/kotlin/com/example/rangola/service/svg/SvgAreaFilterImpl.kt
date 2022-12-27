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

    val gothenburgViewbox = "0 500 100 10"
    val gothenburgCodes = mapOf(
        "1401" to "Härryda",
        "1402" to "Partille",
        "1407" to "Öckerö",
        "1415" to "Stenungsund",
        "1419" to "Tjörn",
        "1421" to "Orust",
        "1427" to "Sotenäs",
        "1430" to "Munkedal",
        "1435" to "Tanum",
        "1438" to "Dals-Ed",
        "1439" to "Färgelanda",
        "1440" to "Ale",
        "1441" to "Lerum",
        "1442" to "Vårgårda",
        "1443" to "Bollebygd",
        "1444" to "Grästorp",
        "1445" to "Essunga",
        "1446" to "Karlsborg",
        "1447" to "Gullspång",
        "1452" to "Tranemo",
        "1460" to "Bengtsfors",
        "1461" to "Mellerud",
        "1462" to "Lilla Edet",
        "1463" to "Mark",
        "1465" to "Svenljunga",
        "1466" to "Herrljunga",
        "1470" to "Vara",
        "1471" to "Götene",
        "1472" to "Tibro",
        "1473" to "Töreboda",
        "1480" to "Göteborg",
        "1481" to "Mölndal",
        "1482" to "Kungälv",
        "1484" to "Lysekil",
        "1485" to "Uddevalla",
        "1486" to "Strömstad",
        "1487" to "Vänersborg",
        "1488" to "Trollhättan",
        "1489" to "Alingsås",
        "1490" to "Borås",
        "1491" to "Ulricehamn",
        "1492" to "Åmål",
        "1493" to "Mariestad",
        "1494" to "Lidköping",
        "1495" to "Skara",
        "1496" to "Skövde",
        "1497" to "Hjo",
        "1498" to "Tidaholm",
        "1499" to "Falköping",
    )

    val dalarnaViewBox = "40 400 100 10"
    val dalarnaCodes: Map<String, String> = mapOf(
        "2021" to "Vansbro",
        "2023" to "Malung-Sälen",
        "2026" to "Gagnef",
        "2029" to "Leksand",
        "2031" to "Rättvik",
        "2034" to "Orsa",
        "2039" to "Älvdalen",
        "2061" to "Smedjebacken",
        "2062" to "Mora",
        "2080" to "Falun",
        "2081" to "Borlänge",
        "2082" to "Säter",
        "2083" to "Hedemora",
        "2084" to "Avesta",
        "2085" to "Ludvika"
    )

    override fun stockholm(document: Document): Document {
        return filter(document, stockholmCodes, stockholmViewBox)
    }

    override fun gothenburg(document: Document): Document {
        return filter(document, gothenburgCodes, gothenburgViewbox)
    }


    override fun dalarna(document: Document): Document {
        return filter(document, dalarnaCodes, dalarnaViewBox)
    }

    fun filter(document: Document, codes: Map<String, String>, viewBox: String): Document {
        val nodeList = document.getElementsByTagName("*")

        var toRemove = false

        //Hard coded towards the sweden map
        var currNode = nodeList.item(1)
        while (currNode != null) {
            if (currNode.nodeType == Node.ELEMENT_NODE && (currNode.nodeName == "polygon" || currNode.nodeName == "g")) {
                val nodeId = currNode.attributes.getNamedItem("id").nodeValue
                toRemove = !codes.containsKey(nodeId)
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

        applyViewBox(document, viewBox)

        return document
    }

    fun applyViewBox(document: Document, viewBox: String) {
        val svgTag = document.getElementsByTagName("svg").item(0) as Element
        svgTag.setAttribute("viewBox", viewBox)
    }

}
package com.example.rangola

import com.example.rangola.dto.Conditional
import com.example.rangola.dto.Order
import com.example.rangola.service.SvgFiller
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MunicipalityCodeMapperApplication

fun main(args: Array<String>) {
    runApplication<MunicipalityCodeMapperApplication>(*args)
}

fun testOrder(): Order {
    return Order(
        "/Users/markusjohansson/Downloads/Kartor 2019.xlsx", listOf(
            Conditional(1) { value: String -> "Entreprenad" == (value) },
            Conditional(2) { value: String -> "Egen regi" == (value) },
        )
    )
}

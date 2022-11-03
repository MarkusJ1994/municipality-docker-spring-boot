package com.example.rangola.dto

data class RowEntry(val municipalityName: String, val municipalityCode: String, val value: String, val colorCode: Int) :
    Comparable<RowEntry> {

    override fun compareTo(other: RowEntry): Int {
        return municipalityName.compareTo(other.municipalityName)
    }
}

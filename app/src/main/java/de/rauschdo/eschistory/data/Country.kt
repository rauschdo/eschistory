package de.rauschdo.eschistory.data

import com.google.gson.annotations.SerializedName

class CountryList : ArrayList<Country>()

data class Country(
    @SerializedName("countryAlpha2Code")
    val countryAlpha2Code: String,
    @SerializedName("countryAlpha3Code")
    val countryAlpha3Code: String,
    @SerializedName("countryName")
    val countryName: String
)
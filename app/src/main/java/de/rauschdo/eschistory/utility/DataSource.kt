package de.rauschdo.eschistory.utility

import android.content.Context
import com.google.gson.GsonBuilder
import de.rauschdo.eschistory.data.ContestsList
import de.rauschdo.eschistory.data.CountryList
import java.io.InputStream

object DataSource {

    fun create(context: Context) = context.loadContestData()

    private fun Context.loadContestData(): ContestsList? =
        parseJson("data/contests.json")

    private fun Context.loadCountryAndTerritories(): CountryList? =
        parseJson("data/countries_and_territories.json")

    private inline fun <reified T : Any> Context.parseJson(assetPath: String): T? {
        val json: String?
        val inputStream: InputStream = resources.assets.open(assetPath)
        json = inputStream.bufferedReader().use { it.readText() }
        val gson = GsonBuilder().create()
        return try {
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
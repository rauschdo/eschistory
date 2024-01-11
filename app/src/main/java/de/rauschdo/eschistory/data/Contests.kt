package de.rauschdo.eschistory.data

import com.google.gson.annotations.SerializedName

class ContestsList : ArrayList<Contest>()

data class Contest(
    @SerializedName("bulletpoints") val bulletpoints: List<String>,
    @SerializedName("hostCountry") val hostCountry: String,
    @SerializedName("issue") val issue: Int,
    @SerializedName("location") val location: Location,
    @SerializedName("participants") val participants: String,
    @SerializedName("winner") val winner: List<String>,
    @SerializedName("year") val year: Int
) {
    data class Location(
        @SerializedName("bulletpoints") val bulletpoints: List<String>,
        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
        @SerializedName("name") val name: String,
        @SerializedName("venue") val venue: String
    )
}
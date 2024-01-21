package de.rauschdo.eschistory.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.realm.kotlin.types.RealmList
import java.lang.reflect.Type

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

fun List<DbContest>.mapToContests(): List<Contest> {
    val stringListToken = object : TypeToken<List<String>>() {}.type
    val gson = Gson()
    return this.mapNotNull { contest ->
        contest.location?.mapToContestLocation(gson, stringListToken)?.let { itLocation ->
            Contest(
                issue = contest.issue,
                bulletpoints = gson.fromJson(contest.bulletpoints, stringListToken),
                hostCountry = contest.hostCountry,
                location = itLocation,
                participants = contest.participants,
                winner = gson.fromJson(contest.winner, stringListToken),
                year = contest.year
            )
        }
    }
}

private fun DbContestLocation.mapToContestLocation(gson: Gson, stringListToken: Type) =
    Contest.Location(
        bulletpoints = gson.fromJson(this.bulletpoints, stringListToken),
        lat = this.lat,
        lng = this.lng,
        name = this.name,
        venue = this.venue
    )
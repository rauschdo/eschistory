package de.rauschdo.eschistory.data

import com.google.gson.Gson
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class DbContest : EmbeddedRealmObject {
    var issue: Int = -1
    var bulletpoints: String? = null
    var hostCountry: String = ""
    var location: DbContestLocation? = null
    var participants: String = ""
    var winner: String? = null
    var year: Int = -1
}

class DbContestLocation : EmbeddedRealmObject {
    // CANNOT have primary key
    var bulletpoints: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    var name: String = ""
    var venue: String = ""
}

fun ContestsList.mapToDbDataset() = DbDataset().apply {
    version = this@mapToDbDataset.datasetVersion
    contests = this@mapToDbDataset.contests.mapToDbContests()
}

private fun List<ContestsList.Contest>.mapToDbContests(): RealmList<DbContest> {
    val gson = Gson()
    val list = realmListOf<DbContest>()
    list.addAll(
        this.map { contest ->
            DbContest().apply {
                issue = contest.issue
                bulletpoints = gson.toJson(contest.bulletpoints)
                hostCountry = contest.hostCountry
                location = contest.location.mapToDbContestLocation(gson)
                participants = contest.participants
                winner = gson.toJson(contest.winner)
                year = contest.year
            }
        }
    )
    return list
}

private fun ContestsList.Contest.Location.mapToDbContestLocation(gson: Gson) =
    DbContestLocation().apply {
        bulletpoints = gson.toJson(this@mapToDbContestLocation.bulletpoints)
        lat = this@mapToDbContestLocation.lat
        lng = this@mapToDbContestLocation.lng
        name = this@mapToDbContestLocation.name
        venue = this@mapToDbContestLocation.venue
    }
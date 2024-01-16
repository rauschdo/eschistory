package de.rauschdo.eschistory.data

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class DbContest : RealmObject { // Empty constructor required by Realm
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var issue: Int = -1

    var bulletpoints: List<String> = realmListOf()
    var hostCountry: String = ""
    var location: DbContestLocation? = null
    var participants: String = ""
    var winner: List<String> = realmListOf()
    var year: Int = -1
}

class DbContestLocation : EmbeddedRealmObject {
    // CANNOT have primary key
    var bulletpoints: List<String> = realmListOf()
    var lat: Double = 0.0
    var lng: Double = 0.0
    var name: String = ""
    var venue: String = ""
}

fun ContestsList.mapToDbContests() = realmListOf<DbContest>().addAll(
    this.map { contest ->
        DbContest().apply {
            issue = contest.issue
            bulletpoints = contest.bulletpoints
            hostCountry = contest.hostCountry
            location = contest.location.mapToDbContestLocation()
            participants = contest.participants
            winner = contest.winner
            year = contest.year
        }
    }
)

private fun Contest.Location.mapToDbContestLocation() = DbContestLocation().apply {
    bulletpoints = this@mapToDbContestLocation.bulletpoints
    lat = this@mapToDbContestLocation.lat
    lng = this@mapToDbContestLocation.lng
    name = this@mapToDbContestLocation.name
    venue = this@mapToDbContestLocation.venue
}
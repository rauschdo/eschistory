package de.rauschdo.eschistory.data

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DbDataset : RealmObject { // Empty constructor required by Realm
    @PrimaryKey
    var version: Long = -1L
    var contests: RealmList<DbContest> = realmListOf()
}
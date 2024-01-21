package de.rauschdo.eschistory.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.rauschdo.eschistory.di.DispatcherProvider
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// sample for realm kotlin di setup
@Singleton
class RealmHelper @Inject constructor(
    @ApplicationContext val context: Context,
    private val realm: Realm,
    private val dispatchers: DispatcherProvider
) {

    suspend fun insertContestsDataset(contestsForDb: RealmList<DbContest>) {
        realm.write {
            // Copy all objects to the realm to return managed instances
            contestsForDb.map {
                copyToRealm(it)
            }
        }
    }

    // https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/read/#find-objects-of-a-type
    val contestFlow: Flow<ResultsChange<DbContest>> = realm.query(DbContest::class).asFlow()
    val asyncCall: Deferred<Unit> = CoroutineScope(dispatchers.io()).async {
        contestFlow.collect { results ->
            when (results) {
                // print out initial results
                is InitialResults<DbContest> -> {
                    results.list.forEach {
                        Timber.i("Contest $it")
                    }
                }

                else -> {
                    // do nothing on changes
                }
            }
        }
    }
}
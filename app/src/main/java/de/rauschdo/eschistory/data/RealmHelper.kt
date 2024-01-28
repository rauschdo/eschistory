package de.rauschdo.eschistory.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.rauschdo.eschistory.di.DispatcherProvider
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// sample for realm kotlin di setup
@Singleton
class RealmHelper @Inject constructor(
    @ApplicationContext val context: Context,
    private val realm: Realm,
    private val dispatchers: DispatcherProvider
) {

    fun handleContestDataset(contestsList: ContestsList) {
        CoroutineScope(dispatchers.io()).launch {
            // first check against version
            realm.query(DbDataset::class).asFlow().collectLatest { dbDatasets ->
                when (dbDatasets) {
                    is InitialResults<DbDataset> -> {
                        dbDatasets.list.find { it.version == contestsList.datasetVersion }?.also {
                            if (contestsList.datasetVersion > it.version) {
                                // newer version
                                realm.write { copyToRealm(contestsList.mapToDbDataset()) }
                            }
                        }
                        // this version is not in database
                            ?: realm.write { copyToRealm(contestsList.mapToDbDataset()) }
                    }

                    else -> {
                        // do nothing on changes
                    }
                }
            }
        }
    }

    // https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/crud/read/#find-objects-of-a-type
    val datasetFlow: Flow<ResultsChange<DbDataset>> = realm.query(clazz = DbDataset::class).asFlow()
}
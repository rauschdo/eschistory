package de.rauschdo.eschistory.utility.proto

import androidx.datastore.core.DataStore
import de.rauschdo.eschistory.ContestItemProto
import de.rauschdo.eschistory.ContestsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

// TODO injectable
// https://proandroiddev.com/android-proto-datastore-should-you-use-it-36ae997d00f2
class ContestItemsRepository(private val contestListDataStore: DataStore<ContestsProto>) {

    val contestListFlow: Flow<ContestsProto> = contestListDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(ContestsProto.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun fetchCachedContestList() = contestListFlow.first()

    suspend fun updateContestList(contest: ContestItemProto) {
        contestListDataStore.updateData { items ->
            items.toBuilder().addItems(contest).build()
        }
    }

    suspend fun removeContest(contest: ContestItemProto) {
        contestListDataStore.updateData { items ->
            val index = items.itemsList.indexOf(contest)
            items.toBuilder().removeItems(index).build()
        }
    }

}
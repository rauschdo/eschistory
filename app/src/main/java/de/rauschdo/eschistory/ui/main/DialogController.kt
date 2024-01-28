package de.rauschdo.eschistory.ui.main

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.qualifiers.ApplicationContext
import de.rauschdo.eschistory.DIALOG_ANIMATION_TIME
import de.rauschdo.eschistory.data.DialogModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogController @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val dialogModelQueue: MutableState<List<DialogModel>> = mutableStateOf(emptyList())
    val dialogQueue: State<List<DialogModel>> = dialogModelQueue

    val showsDialog: Boolean
        get() = dialogModelQueue.value.getOrNull(0) != null

    fun requestDialog(model: DialogModel?) {
        model?.let { itModel ->
            dialogModelQueue.value = dialogModelQueue.value.toMutableList().apply {
                // avoid duplicated entries in queue
                val existingDialogModelInQueue = indexOfFirst { it.id == itModel.id } != -1
                if (!existingDialogModelInQueue) {
                    add(itModel)
                }
            }
        }
    }

    fun consumeDialog(scope: CoroutineScope) {
        scope.launch {
            val currentQueue = dialogModelQueue.value.toMutableList()

            // wait for old instance to animate out
            dialogModelQueue.value = emptyList()
            delay(DIALOG_ANIMATION_TIME)

            dialogModelQueue.value = currentQueue.apply {
                try {
                    removeAt(0)
                } catch (e: IndexOutOfBoundsException) {
                    // ignore (empty queue)
                }
            }
        }
    }
}
package de.rauschdo.eschistory.utility

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper to forward context where necessary,
 * reducing forwards Screen -> ViewModel
 */
@Singleton
class ContextProvider @Inject constructor(
    @ApplicationContext val context: Context
)
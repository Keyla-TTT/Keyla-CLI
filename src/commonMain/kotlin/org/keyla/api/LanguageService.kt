package org.keyla.api

import org.keyla.models.*

interface LanguageService {
    suspend fun getDictionaries(): DictionariesResponse
    suspend fun getMergers(): MergersResponse
    suspend fun getModifiers(): ModifiersResponse
} 
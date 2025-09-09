package org.keyla.api

import org.keyla.models.*

/**
 * Service interface for managing language-related data.
 *
 * This service provides access to language configuration data including dictionaries,
 * mergers, and modifiers used in typing tests. It supports multiple languages and
 * provides the necessary data structures for text generation and validation.
 */
interface LanguageService {
    /**
     * Retrieves all available language dictionaries.
     *
     * @return A DictionariesResponse containing all language dictionaries
     */
    suspend fun getDictionaries(): DictionariesResponse

    /**
     * Retrieves all available language mergers.
     *
     * @return A MergersResponse containing all language merger configurations
     */
    suspend fun getMergers(): MergersResponse

    /**
     * Retrieves all available language modifiers.
     *
     * @return A ModifiersResponse containing all language modifier configurations
     */
    suspend fun getModifiers(): ModifiersResponse
}

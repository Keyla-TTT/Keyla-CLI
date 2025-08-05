package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class LanguageServiceTest {
    @Test
    fun `DictionariesResponse should have correct structure`() {
        val response =
            DictionariesResponse(
                dictionaries =
                    listOf(
                        DictionaryInfo("english"),
                        DictionaryInfo("italian"),
                        DictionaryInfo("spanish"),
                        DictionaryInfo("french"),
                    ),
            )

        assertEquals(4, response.dictionaries.size)
        assertEquals("english", response.dictionaries[0].name)
        assertEquals("italian", response.dictionaries[1].name)
        assertEquals("spanish", response.dictionaries[2].name)
        assertEquals("french", response.dictionaries[3].name)
    }

    @Test
    fun `MergersResponse should have correct structure`() {
        val response =
            MergersResponse(
                mergers =
                    listOf(
                        MergerInfo(
                            name = "standard",
                            description = "Standard word merger",
                        ),
                        MergerInfo(
                            name = "random",
                            description = "Random word selection",
                        ),
                        MergerInfo(
                            name = "frequency",
                            description = "Frequency-based word selection",
                        ),
                    ),
            )

        assertEquals(3, response.mergers.size)
        assertEquals("standard", response.mergers[0].name)
        assertEquals("Standard word merger", response.mergers[0].description)
        assertEquals("random", response.mergers[1].name)
        assertEquals("Random word selection", response.mergers[1].description)
        assertEquals("frequency", response.mergers[2].name)
        assertEquals("Frequency-based word selection", response.mergers[2].description)
    }

    @Test
    fun `ModifiersResponse should have correct structure`() {
        val response =
            ModifiersResponse(
                modifiers =
                    listOf(
                        ModifierInfo(
                            name = "punctuation",
                            description = "Include punctuation marks",
                        ),
                        ModifierInfo(
                            name = "numbers",
                            description = "Include numbers",
                        ),
                        ModifierInfo(
                            name = "symbols",
                            description = "Include special symbols",
                        ),
                        ModifierInfo(
                            name = "capitalization",
                            description = "Include capital letters",
                        ),
                    ),
            )

        assertEquals(4, response.modifiers.size)
        assertEquals("punctuation", response.modifiers[0].name)
        assertEquals("Include punctuation marks", response.modifiers[0].description)
        assertEquals("numbers", response.modifiers[1].name)
        assertEquals("Include numbers", response.modifiers[1].description)
        assertEquals("symbols", response.modifiers[2].name)
        assertEquals("Include special symbols", response.modifiers[2].description)
        assertEquals("capitalization", response.modifiers[3].name)
        assertEquals("Include capital letters", response.modifiers[3].description)
    }

    @Test
    fun `DictionaryInfo should have correct structure`() {
        val info = DictionaryInfo("english")

        assertEquals("english", info.name)
    }

    @Test
    fun `MergerInfo should have correct structure`() {
        val info =
            MergerInfo(
                name = "standard",
                description = "Standard word merger",
            )

        assertEquals("standard", info.name)
        assertEquals("Standard word merger", info.description)
    }

    @Test
    fun `ModifierInfo should have correct structure`() {
        val info =
            ModifierInfo(
                name = "punctuation",
                description = "Include punctuation marks",
            )

        assertEquals("punctuation", info.name)
        assertEquals("Include punctuation marks", info.description)
    }

    @Test
    fun `LanguagesResponse should have correct structure`() {
        val response =
            LanguagesResponse(
                languages =
                    listOf(
                        LanguageInfo(
                            language = "english",
                            dictionaries = listOf("en_US", "en_GB"),
                        ),
                        LanguageInfo(
                            language = "italian",
                            dictionaries = listOf("it_IT"),
                        ),
                    ),
            )

        assertEquals(2, response.languages.size)
        assertEquals("english", response.languages[0].language)
        assertEquals(listOf("en_US", "en_GB"), response.languages[0].dictionaries)
        assertEquals("italian", response.languages[1].language)
        assertEquals(listOf("it_IT"), response.languages[1].dictionaries)
    }

    @Test
    fun `LanguageInfo should have correct structure`() {
        val info =
            LanguageInfo(
                language = "english",
                dictionaries = listOf("en_US", "en_GB"),
            )

        assertEquals("english", info.language)
        assertEquals(listOf("en_US", "en_GB"), info.dictionaries)
    }
}

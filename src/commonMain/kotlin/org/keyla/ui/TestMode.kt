package org.keyla.ui

import com.varabyte.kotter.foundation.input.name
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.keyla.models.*
import org.keyla.util.calculateTypingStats
import org.keyla.util.format1f
import org.keyla.util.getCurrentTimeMillis

sealed class TestScreen {
    object MainMenu : TestScreen()

    object SelectDictionaries : TestScreen()

    object SelectMergers : TestScreen()

    object SelectModifiers : TestScreen()

    object SelectWordCount : TestScreen()

    object TypingTest : TestScreen()

    object TestResults : TestScreen()

    object Error : TestScreen()

    object Success : TestScreen()

    object Info : TestScreen()

    object ContinueLastTest : TestScreen()
}

data class TestState(
    val currentProfile: ProfileResponse? = null,
    val availableDictionaries: List<DictionaryInfo> = emptyList(),
    val availableMergers: List<MergerInfo> = emptyList(),
    val availableModifiers: List<ModifierInfo> = emptyList(),
    val selectedDictionaries: MutableList<SourceWithMerger> = mutableListOf(),
    val selectedModifiers: MutableList<String> = mutableListOf(),
    val selectedWordCount: Int? = null,
    val currentTest: TestResponse? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val infoMessage: String? = null,
)

suspend fun testMode(
    testService: org.keyla.api.TestService,
    languageService: org.keyla.api.LanguageService,
    initialProfile: ProfileResponse?,
    initialLanguages: List<LanguageInfo>,
) {
    coroutineScope {
        session {
            var screen by liveVarOf<TestScreen>(if (initialProfile == null) TestScreen.Error else TestScreen.MainMenu)
            var state by liveVarOf(TestState(currentProfile = initialProfile))

            var mainMenuIndex by liveVarOf(0)
            var dictionaryIndex by liveVarOf(0)
            var mergerIndex by liveVarOf(0)
            var modifierIndex by liveVarOf(0)
            var wordCountIndex by liveVarOf(1)

            var currentWordIndex by liveVarOf(0)
            var currentInput by liveVarOf("")
            var completedWords by liveVarOf(emptyList<String>())
            var errorCount by liveVarOf(0)
            var startTime by liveVarOf(0L)
            var isComplete by liveVarOf(false)
            var totalChars by liveVarOf(0)
            var correctChars by liveVarOf(0)
            var uncorrectedErrors by liveVarOf(0)
            var showRestartButton by liveVarOf(false)
            var errorWordIndices by liveVarOf(mutableSetOf<Int>())

            var searchInput by liveVarOf("")
            var isSearchMode by liveVarOf(false)
            var filteredDictionaries by liveVarOf(emptyList<DictionaryInfo>())

            fun updateState(newState: TestState) {
                state = newState
            }

            fun setScreen(newScreen: TestScreen) {
                screen = newScreen
            }

            fun resetTestState() {
                currentWordIndex = 0
                currentInput = ""
                completedWords = emptyList()
                errorCount = 0
                startTime = 0L
                isComplete = false
                totalChars = 0
                correctChars = 0
                uncorrectedErrors = 0
                showRestartButton = false
                errorWordIndices.clear()
            }

            fun updateFilteredDictionaries() {
                if (searchInput.isBlank()) {
                    filteredDictionaries = state.availableDictionaries
                } else {
                    filteredDictionaries =
                        state.availableDictionaries.filter {
                            it.name.contains(searchInput, ignoreCase = true)
                        }
                }
                if (dictionaryIndex >= filteredDictionaries.size) {
                    dictionaryIndex = 0
                }
            }

            section {
                fun renderWordWithProgress(
                    word: String,
                    input: String,
                ) {
                    if (input.length <= word.length) {
                        val correctPart = word.take(input.length)
                        val remainingPart = word.drop(input.length)
                        if (input == correctPart) {
                            green { text(correctPart) }
                        } else {
                            red { text(correctPart) }
                        }
                        text(remainingPart)
                    } else {
                        red { text(word) }
                    }
                }

                fun renderHeader(title: String) {
                    green { textLine("╔══════════════════════════════════════════════════════════════╗") }
                    green { textLine("║                         $title                    ║") }
                    green { textLine("╚══════════════════════════════════════════════════════════════╝") }
                    textLine()
                }

                fun renderGoBackOption() {
                    textLine()
                    cyan { textLine("> Go Back") }
                    textLine()
                    yellow { textLine("Press Enter to go back") }
                }

                when (screen) {
                    is TestScreen.MainMenu -> {
                        renderHeader("KEYLA TYPING TEST")
                        textLine("Profile: ${state.currentProfile?.name ?: "None"}")
                        textLine()
                        val options =
                            listOf(
                                "1. Start New Test",
                                "2. Continue Last Test",
                                "3. Exit",
                            )
                        options.forEachIndexed { i, opt ->
                            if (i == mainMenuIndex) {
                                cyan { textLine("> $opt") }
                            } else {
                                textLine("  $opt")
                            }
                        }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select") }
                    }
                    is TestScreen.SelectDictionaries -> {
                        if (filteredDictionaries.isEmpty()) {
                            updateFilteredDictionaries()
                        }

                        renderHeader("SELECT DICTIONARIES")
                        textLine("Selected: ${state.selectedDictionaries.size} dictionary(ies)")
                        if (state.selectedDictionaries.isNotEmpty()) {
                            textLine("Current selection:")
                            state.selectedDictionaries.forEachIndexed { i, source ->
                                val mergerText = if (i == 0) " (primary - no merger needed)" else source.merger?.let { " (merger: $it)" } ?: " (no merger)"
                                textLine("  ${i + 1}. ${source.name}$mergerText")
                            }
                            textLine()
                        }

                        if (isSearchMode) {
                            textLine("Search: $searchInput")
                            yellow { textLine("Type to search, Enter to confirm") }
                        } else {
                            textLine("Search: $searchInput")
                            yellow { textLine("Press '/ or S' to search, arrow keys to navigate") }
                        }
                        textLine()

                        textLine("Available dictionaries:")
                        val maxDisplay = 15
                        val startIndex = (dictionaryIndex / maxDisplay) * maxDisplay
                        val endIndex = minOf(startIndex + maxDisplay, filteredDictionaries.size)

                        if (startIndex > 0) {
                            yellow { textLine("... (showing ${startIndex + 1}-$endIndex of ${filteredDictionaries.size})") }
                        }

                        for (i in startIndex until endIndex) {
                            val dict = filteredDictionaries[i]
                            val displayIndex = i - startIndex
                            if (displayIndex == dictionaryIndex % maxDisplay) {
                                cyan { textLine("> ${dict.name}") }
                            } else {
                                textLine("  ${dict.name}")
                            }
                        }

                        if (endIndex < filteredDictionaries.size) {
                            yellow { textLine("... (use arrow keys to see more)") }
                        }

                        textLine()
                        if (state.selectedDictionaries.isNotEmpty()) {
                            if (dictionaryIndex == filteredDictionaries.size) {
                                cyan { textLine("> Done selecting dictionaries") }
                            } else {
                                textLine("  Done selecting dictionaries")
                            }
                            textLine()
                            yellow { textLine("Use arrow keys to navigate, Enter to select, Tab to jump to 'Done'") }
                        } else {
                            yellow { textLine("Use arrow keys to navigate, Enter to select") }
                        }
                    }
                    is TestScreen.SelectMergers -> {
                        renderHeader("SELECT MERGER")
                        textLine("Dictionary: ${state.selectedDictionaries.last().name}")
                        textLine("Note: This merger is MANDATORY for additional dictionaries")
                        textLine()
                        textLine("Available mergers:")
                        state.availableMergers.forEachIndexed { i, merger ->
                            if (i == mergerIndex) {
                                cyan { textLine("> ${merger.name} - ${merger.description}") }
                            } else {
                                textLine("  ${merger.name} - ${merger.description}")
                            }
                        }
                        textLine()
                        yellow { textLine("Select a merger to continue (mandatory)") }
                    }
                    is TestScreen.SelectModifiers -> {
                        renderHeader("SELECT MODIFIERS")
                        textLine("Selected: ${state.selectedModifiers.size} modifier(s)")
                        if (state.selectedModifiers.isNotEmpty()) {
                            textLine("Current selection:")
                            state.selectedModifiers.forEachIndexed { i, modifier ->
                                textLine("  ${i + 1}. $modifier")
                            }
                            textLine()
                        }
                        textLine("Available modifiers:")
                        state.availableModifiers.forEachIndexed { i, modifier ->
                            if (i == modifierIndex) {
                                cyan { textLine("> ${modifier.name} - ${modifier.description}") }
                            } else {
                                textLine("  ${modifier.name} - ${modifier.description}")
                            }
                        }
                        textLine()
                        cyan { textLine("> Skip modifiers") }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select, Tab to jump to 'Skip'") }
                    }
                    is TestScreen.SelectWordCount -> {
                        renderHeader("SELECT WORD COUNT")
                        textLine("Dictionaries: ${state.selectedDictionaries.size}")
                        textLine("Modifiers: ${state.selectedModifiers.size}")
                        textLine()
                        val wordCounts = listOf(10, 25, 50, 100)
                        wordCounts.forEachIndexed { i, count ->
                            if (i == wordCountIndex) {
                                cyan { textLine("> $count words") }
                            } else {
                                textLine("  $count words")
                            }
                        }
                        renderGoBackOption()
                    }
                    is TestScreen.TypingTest -> {
                        state.currentTest?.let { test ->
                            renderHeader("TYPING TEST")
                            textLine("Dictionaries: ${test.sources.size}")
                            textLine("Modifiers: ${test.modifiers.size}")
                            textLine("Progress: ${completedWords.size}/${test.words.size}")
                            textLine()

                            val currentTime = if (startTime > 0) getCurrentTimeMillis() - startTime else 0L
                            val stats = calculateTypingStats(totalChars, correctChars, uncorrectedErrors, currentTime)

                            blue {
                                textLine(
                                    "WPM: ${format1f(
                                        stats.netWPM,
                                    )} | Accuracy: ${format1f(stats.accuracy)}% | Time: ${format1f(stats.timeSeconds)}s",
                                )
                            }
                            blue {
                                textLine(
                                    "Gross WPM: ${format1f(
                                        stats.grossWPM,
                                    )} | Adjusted WPM: ${format1f(stats.adjustedWPM)} | Errors: $uncorrectedErrors",
                                )
                            }
                            textLine()

                            completedWords.takeLast(3).forEach { word -> green { text("$word ") } }
                            if (currentWordIndex < test.words.size) {
                                val currentWord = test.words[currentWordIndex]
                                renderWordWithProgress(currentWord, currentInput)
                                text(" ")
                            }
                            test.words.drop(currentWordIndex + 1).take(5).forEach { word -> text("$word ") }
                            textLine()
                            textLine()
                            textLine("Type: $currentInput")
                            if (isComplete) {
                                textLine()
                                green { textLine("Test Complete! Submitting results...") }
                            }
                            if (showRestartButton) {
                                textLine()
                                cyan { textLine("> Restart Test (Press Enter)") }
                            }
                            textLine()
                            yellow { textLine("Press Tab + Enter to restart test") }
                        }
                    }
                    is TestScreen.TestResults -> {
                        renderHeader("TEST RESULTS")
                        state.currentTest?.let { test ->
                            val testTime = if (startTime > 0) getCurrentTimeMillis() - startTime else 0L
                            val stats = calculateTypingStats(totalChars, correctChars, uncorrectedErrors, testTime)

                            blue { textLine("Net WPM: ${format1f(stats.netWPM)}") }
                            blue { textLine("Gross WPM: ${format1f(stats.grossWPM)}") }
                            blue { textLine("Adjusted WPM: ${format1f(stats.adjustedWPM)}") }
                            blue { textLine("Accuracy: ${format1f(stats.accuracy)}%") }
                            blue { textLine("Total Characters: ${stats.totalChars}") }
                            blue { textLine("Correct Characters: ${stats.correctChars}") }
                            blue { textLine("Uncorrected Errors: ${stats.uncorrectedErrors}") }
                            blue { textLine("Time: ${format1f(stats.timeSeconds)}s") }
                        }
                        textLine()
                        green { textLine("Results submitted successfully!") }
                        renderGoBackOption()
                    }
                    is TestScreen.ContinueLastTest -> {
                        renderHeader("CONTINUE LAST TEST")
                        yellow { textLine("Feature not implemented yet") }
                        renderGoBackOption()
                    }
                    is TestScreen.Error -> {
                        red { textLine("ERROR: ${state.errorMessage}") }
                        renderGoBackOption()
                    }
                    is TestScreen.Success -> {
                        green { textLine("SUCCESS: ${state.successMessage}") }
                        renderGoBackOption()
                    }
                    is TestScreen.Info -> {
                        cyan { textLine("INFO: ${state.infoMessage}") }
                        renderGoBackOption()
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    when (screen) {
                        is TestScreen.MainMenu -> {
                            when (key.name) {
                                "UP" -> {
                                    mainMenuIndex = (mainMenuIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    mainMenuIndex = (mainMenuIndex + 1).coerceAtMost(2)
                                }
                                "ENTER" -> {
                                    when (mainMenuIndex) {
                                        0 -> {
                                            launch {
                                                handleLoadTestData(languageService, state, ::updateState, ::setScreen)
                                            }
                                        }
                                        1 ->
                                            launch {
                                                handleContinueLastTest(
                                                    testService,
                                                    state,
                                                    ::updateState,
                                                    ::setScreen,
                                                    ::resetTestState,
                                                )
                                            }
                                        2 -> signal()
                                    }
                                }
                                "ESCAPE" -> signal()
                            }
                        }
                        is TestScreen.SelectDictionaries -> {
                            val maxDisplay = 15
                            val startIndex = (dictionaryIndex / maxDisplay) * maxDisplay
                            val endIndex = minOf(startIndex + maxDisplay, filteredDictionaries.size)
                            val doneOptionIndex = filteredDictionaries.size
                            val totalOptions = filteredDictionaries.size + (if (state.selectedDictionaries.isNotEmpty()) 1 else 0)

                            when (key.name) {
                                "SLASH", "/", "s", "S" -> {
                                    if (!isSearchMode) {
                                        isSearchMode = true
                                        searchInput = ""
                                        updateFilteredDictionaries()
                                    }
                                }
                                "ESCAPE" -> {
                                    if (isSearchMode) {
                                        isSearchMode = false
                                        searchInput = ""
                                        updateFilteredDictionaries()
                                    }
                                }
                                "ENTER" -> {
                                    if (isSearchMode) {
                                        isSearchMode = false
                                        updateFilteredDictionaries()
                                    } else if (dictionaryIndex < filteredDictionaries.size) {
                                        val selectedDict = filteredDictionaries[dictionaryIndex]
                                        if (state.selectedDictionaries.isEmpty()) {
                                            val newSelectedDictionaries = state.selectedDictionaries.toMutableList()
                                            newSelectedDictionaries.add(SourceWithMerger(selectedDict.name))
                                            state = state.copy(selectedDictionaries = newSelectedDictionaries)
                                            screen = TestScreen.SelectDictionaries
                                        } else {
                                            val newSelectedDictionaries = state.selectedDictionaries.toMutableList()
                                            newSelectedDictionaries.add(SourceWithMerger(selectedDict.name))
                                            state = state.copy(selectedDictionaries = newSelectedDictionaries)
                                            screen = TestScreen.SelectMergers
                                        }
                                    } else if (dictionaryIndex == doneOptionIndex && state.selectedDictionaries.isNotEmpty()) {
                                        screen = TestScreen.SelectModifiers
                                    }
                                }
                                "BACKSPACE" -> {
                                    if (isSearchMode && searchInput.isNotEmpty()) {
                                        searchInput = searchInput.dropLast(1)
                                        updateFilteredDictionaries()
                                    }
                                }
                                else -> {
                                    if (isSearchMode) {
                                        if (key.name.length == 1) {
                                            searchInput += key.name
                                            updateFilteredDictionaries()
                                        }
                                    } else {
                                        when (key.name) {
                                            "UP" -> {
                                                if (dictionaryIndex > 0) {
                                                    dictionaryIndex--
                                                    if (dictionaryIndex < startIndex && dictionaryIndex < filteredDictionaries.size) {
                                                        dictionaryIndex = startIndex - 1
                                                    }
                                                }
                                            }
                                            "DOWN" -> {
                                                if (dictionaryIndex < totalOptions - 1) {
                                                    dictionaryIndex++
                                                    if (dictionaryIndex >= endIndex && dictionaryIndex < filteredDictionaries.size) {
                                                        dictionaryIndex = endIndex
                                                    }
                                                }
                                            }
                                            "TAB" -> {
                                                if (state.selectedDictionaries.isNotEmpty()) {
                                                    dictionaryIndex = doneOptionIndex
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        is TestScreen.SelectMergers -> {
                            when (key.name) {
                                "UP" -> {
                                    mergerIndex = (mergerIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    mergerIndex = (mergerIndex + 1).coerceAtMost(state.availableMergers.size - 1)
                                }
                                "ENTER" -> {
                                    if (mergerIndex < state.availableMergers.size) {
                                        val selectedMerger = state.availableMergers[mergerIndex]
                                        val newSelectedDictionaries = state.selectedDictionaries.toMutableList()
                                        newSelectedDictionaries[newSelectedDictionaries.size - 1] =
                                            newSelectedDictionaries.last().copy(merger = selectedMerger.name)
                                        state = state.copy(selectedDictionaries = newSelectedDictionaries)
                                        screen = TestScreen.SelectDictionaries
                                    }
                                }
                            }
                        }
                        is TestScreen.SelectModifiers -> {
                            val totalOptions = state.availableModifiers.size + 1

                            when (key.name) {
                                "UP" -> {
                                    modifierIndex = (modifierIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    modifierIndex = (modifierIndex + 1).coerceAtMost(totalOptions - 1)
                                }
                                "TAB" -> {
                                    modifierIndex = state.availableModifiers.size
                                }
                                "ENTER" -> {
                                    if (modifierIndex < state.availableModifiers.size) {
                                        val selectedModifier = state.availableModifiers[modifierIndex]
                                        if (!state.selectedModifiers.contains(selectedModifier.name)) {
                                            val newSelectedModifiers = state.selectedModifiers.toMutableList()
                                            newSelectedModifiers.add(selectedModifier.name)
                                            state = state.copy(selectedModifiers = newSelectedModifiers)
                                        }
                                        screen = TestScreen.SelectModifiers
                                    } else if (modifierIndex == state.availableModifiers.size) {
                                        screen = TestScreen.SelectWordCount
                                    }
                                }
                            }
                        }
                        is TestScreen.SelectWordCount -> {
                            when (key.name) {
                                "UP" -> {
                                    wordCountIndex = (wordCountIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    wordCountIndex = (wordCountIndex + 1).coerceAtMost(4)
                                }
                                "ENTER" -> {
                                    if (wordCountIndex < 4) {
                                        val wordCounts = listOf(10, 25, 50, 100)
                                        state = state.copy(selectedWordCount = wordCounts[wordCountIndex])
                                        launch {
                                            handleCreateTest(testService, state, ::updateState, ::setScreen, ::resetTestState)
                                        }
                                    } else if (wordCountIndex == 4) {
                                        screen = TestScreen.SelectModifiers
                                    }
                                }
                            }
                        }
                        is TestScreen.TypingTest -> {
                            when (key.name) {
                                "SPACE" -> {
                                    state.currentTest?.let { test ->
                                        if (currentWordIndex < test.words.size) {
                                            val currentWord = test.words[currentWordIndex]
                                            val typed = currentInput.trim()

                                            if (typed == currentWord) {
                                                totalChars += 1
                                                correctChars += 1
                                                completedWords = completedWords + typed
                                                currentInput = ""
                                                currentWordIndex++
                                                if (currentWordIndex >= test.words.size) {
                                                    isComplete = true
                                                    launch {
                                                        handleSubmitResults(testService, state, ::updateState, ::setScreen, totalChars, correctChars, uncorrectedErrors, startTime, errorWordIndices.toList())
                                                    }
                                                }
                                            } else if (currentInput.length < currentWord.length) {
                                                totalChars += 1
                                                uncorrectedErrors++
                                            }
                                        }
                                    }
                                }
                                "TAB" -> {
                                    showRestartButton = !showRestartButton
                                }
                                "BACKSPACE" -> {
                                    if (currentInput.isNotEmpty()) {
                                        currentInput = currentInput.dropLast(1)
                                    }
                                }
                                "ENTER" -> {
                                    if (showRestartButton) {
                                        launch {
                                            handleCreateTest(testService, state, ::updateState, ::setScreen, ::resetTestState)
                                        }
                                    }
                                }
                                else -> {
                                    if (key.name.length == 1 && !isComplete) {
                                        if (currentWordIndex == 0 && currentInput.isEmpty()) {
                                            startTime = getCurrentTimeMillis()
                                        }

                                        state.currentTest?.let { test ->
                                            if (currentWordIndex < test.words.size) {
                                                val currentWord = test.words[currentWordIndex]
                                                val expectedChar =
                                                    if (currentInput.length < currentWord.length) {
                                                        currentWord[currentInput.length]
                                                    } else {
                                                        null
                                                    }

                                                totalChars++

                                                if (expectedChar != null && key.name[0] == expectedChar) {
                                                    correctChars++
                                                    currentInput += key.name
                                                } else {
                                                    uncorrectedErrors++
                                                    errorWordIndices.add(currentWordIndex)
                                                    currentInput += key.name
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        is TestScreen.TestResults -> {
                            when (key.name) {
                                "ENTER" -> screen = TestScreen.MainMenu
                                else -> screen = TestScreen.MainMenu
                            }
                        }
                        is TestScreen.ContinueLastTest -> {
                            when (key.name) {
                                "ENTER" -> screen = TestScreen.MainMenu
                                else -> screen = TestScreen.MainMenu
                            }
                        }
                        is TestScreen.Error, is TestScreen.Success, is TestScreen.Info -> {
                            when (key.name) {
                                "ENTER" -> {
                                    screen = TestScreen.MainMenu
                                    state =
                                        state.copy(
                                            errorMessage = null,
                                            successMessage = null,
                                            infoMessage = null,
                                        )
                                }
                                else -> {
                                    screen = TestScreen.MainMenu
                                    state =
                                        state.copy(
                                            errorMessage = null,
                                            successMessage = null,
                                            infoMessage = null,
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

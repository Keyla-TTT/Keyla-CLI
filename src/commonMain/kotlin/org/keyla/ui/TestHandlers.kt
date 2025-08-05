package org.keyla.ui

import org.keyla.api.LanguageService
import org.keyla.api.TestService
import org.keyla.models.*
import org.keyla.util.calculateTypingStats
import org.keyla.util.getCurrentTimeMillis

suspend fun handleLoadTestData(
    languageService: LanguageService,
    state: TestState,
    updateState: (TestState) -> Unit,
    setScreen: (TestScreen) -> Unit,
) {
    try {
        val dictionariesResponse = languageService.getDictionaries()
        val mergersResponse = languageService.getMergers()
        val modifiersResponse = languageService.getModifiers()

        updateState(
            state.copy(
                availableDictionaries = dictionariesResponse.dictionaries,
                availableMergers = mergersResponse.mergers,
                availableModifiers = modifiersResponse.modifiers,
            ),
        )
        setScreen(TestScreen.SelectDictionaries)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "loadTestData")))
        setScreen(TestScreen.Error)
    }
}

suspend fun handleCreateTest(
    testService: TestService,
    state: TestState,
    updateState: (TestState) -> Unit,
    setScreen: (TestScreen) -> Unit,
    resetTestState: () -> Unit,
) {
    try {
        val testRequest =
            TestRequest(
                profileId = state.currentProfile!!.id,
                sources = state.selectedDictionaries,
                wordCount = state.selectedWordCount!!,
                modifiers = state.selectedModifiers,
            )

        val test = testService.createTest(testRequest)
        updateState(state.copy(currentTest = test))
        resetTestState()
        setScreen(TestScreen.TypingTest)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "createTest")))
        setScreen(TestScreen.Error)
    }
}

suspend fun handleContinueLastTest(
    testService: TestService,
    state: TestState,
    updateState: (TestState) -> Unit,
    setScreen: (TestScreen) -> Unit,
    resetTestState: () -> Unit,
) {
    try {
        val lastTestResponse = testService.getLastTest(state.currentProfile!!.id)
        if (lastTestResponse.words.isNotEmpty()) {
            val test =
                TestResponse(
                    testId = "last-test",
                    profileId = state.currentProfile.id,
                    words = lastTestResponse.words,
                    sources = emptyList(),
                    modifiers = emptyList(),
                    createdAt = "",
                    completedAt = null,
                    accuracy = null,
                    rawAccuracy = null,
                    testTime = null,
                    errorCount = null,
                    errorWordIndices = emptyList(),
                    timeLimit = lastTestResponse.timeLimit,
                )
            updateState(state.copy(currentTest = test))
            resetTestState()
            setScreen(TestScreen.TypingTest)
        } else {
            updateState(state.copy(infoMessage = "No previous test found"))
            setScreen(TestScreen.Info)
        }
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "continueLastTest")))
        setScreen(TestScreen.Error)
    }
}

suspend fun handleSubmitResults(
    testService: TestService,
    state: TestState,
    updateState: (TestState) -> Unit,
    setScreen: (TestScreen) -> Unit,
    totalChars: Int,
    correctChars: Int,
    uncorrectedErrors: Int,
    startTime: Long,
    errorWordIndices: List<Int>,
) {
    try {
        val testTime = if (startTime > 0) getCurrentTimeMillis() - startTime else 0L
        val stats = calculateTypingStats(totalChars, correctChars, uncorrectedErrors, testTime)

        val resultsRequest =
            TestResultsRequest(
                accuracy = stats.accuracy,
                rawAccuracy = stats.accuracy,
                testTime = testTime,
                errorCount = uncorrectedErrors,
                errorWordIndices = errorWordIndices,
            )

        testService.submitTestResults(state.currentTest!!.testId, resultsRequest)
        setScreen(TestScreen.TestResults)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "submitResults")))
        setScreen(TestScreen.Error)
    }
}

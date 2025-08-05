package org.keyla.ui

fun getErrorMessage(
    exception: Exception,
    context: String,
): String {
    return when {
        exception.message?.contains("Connection refused", ignoreCase = true) == true ->
            "Unable to connect to the server. Please check if the backend is running and try again."
        exception.message?.contains("timeout", ignoreCase = true) == true ->
            "Request timed out. Please check your internet connection and try again."
        exception.message?.contains("404", ignoreCase = true) == true ->
            "The requested resource was not found. Please check your configuration."
        exception.message?.contains("500", ignoreCase = true) == true ->
            "Server error occurred. Please try again later."
        exception.message?.contains("401", ignoreCase = true) == true ->
            "Authentication failed. Please check your credentials."
        exception.message?.contains("403", ignoreCase = true) == true ->
            "Access denied. You don't have permission to perform this action."
        context == "updateConfig" && (
            exception.message?.contains("404", ignoreCase = true) == true ||
                exception.message?.contains("not found", ignoreCase = true) == true ||
                exception.message?.contains("invalid key", ignoreCase = true) == true
        ) ->
            "Failed setting config. Be sure the key exists."
        else ->
            when (context) {
                "loadDictionaries" -> "Failed to load dictionaries. Please try again."
                "createTest" -> "Failed to create test. Please try again."
                "continueLastTest" -> "There is no previous test to continue."
                "viewTestHistory" -> "Failed to load test history. Please try again."
                "changeProfile" -> "Failed to load profiles. Please try again."
                "createProfile" -> "Failed to create profile. Please check your input and try again."
                "viewAllConfig" -> "Failed to load configuration. Please try again."
                "updateConfig" -> "Failed to update configuration. Please try again."
                "submitResults" -> "Failed to submit test results. Please try again."
                "testConnection" -> "Failed to test connection. Please try again."
                "updateBackendUrl" -> "Failed to update backend URL. Please try again."
                else -> "An unexpected error occurred. Please try again."
            }
    }
}

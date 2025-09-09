package org.keyla.ui

object ErrorMessages {
    const val BACKEND_CONNECTION_FAILED = "Failed to connect to the backend. Please check if the backend is running and try again."
    const val BACKEND_SETTINGS_MESSAGE = "You can change the backend URL by running 'keyla settings'"
    const val RESOURCE_NOT_FOUND = "The requested resource was not found. Please check your configuration."
    const val SERVER_ERROR = "Server error occurred. Please try again later."
    const val AUTHENTICATION_FAILED = "Authentication failed. Please check your credentials."
    const val ACCESS_DENIED = "Access denied. You don't have permission to perform this action."
    const val CONFIG_KEY_INVALID = "Failed setting config. Be sure the key exists."
    const val GENERIC_ERROR = "An error occurred. Please try again."
}

suspend fun showConnectionErrorAndExit(platformService: org.keyla.core.interfaces.PlatformService) {
    println(ErrorMessages.BACKEND_CONNECTION_FAILED)
    println(ErrorMessages.BACKEND_SETTINGS_MESSAGE)
    kotlinx.coroutines.delay(1000)
    platformService.exitProcess(1)
}

fun getErrorMessage(exception: Exception, context: String): String {
    val message = exception.message ?: ""
    
    return when {
        isConnectionError(message) -> ErrorMessages.BACKEND_CONNECTION_FAILED
        isHttpError(message, 404) -> ErrorMessages.RESOURCE_NOT_FOUND
        isHttpError(message, 500) -> ErrorMessages.SERVER_ERROR
        isHttpError(message, 401) -> ErrorMessages.AUTHENTICATION_FAILED
        isHttpError(message, 403) -> ErrorMessages.ACCESS_DENIED
        isConfigKeyError(context, message) -> ErrorMessages.CONFIG_KEY_INVALID
        else -> ErrorMessages.GENERIC_ERROR
    }
}

private fun isConnectionError(message: String): Boolean {
    val connectionErrors = listOf(
        "Connection refused", "timeout", "ConnectException", 
        "SocketTimeoutException", "UnknownHostException"
    )
    return connectionErrors.any { message.contains(it, ignoreCase = true) }
}

private fun isHttpError(message: String, code: Int): Boolean {
    return message.contains(code.toString(), ignoreCase = true)
}

private fun isConfigKeyError(context: String, message: String): Boolean {
    return context == "updateConfig" && (
        message.contains("404", ignoreCase = true) ||
        message.contains("not found", ignoreCase = true) ||
        message.contains("invalid key", ignoreCase = true)
    )
}

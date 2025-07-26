package org.keyla.util

data class TypingStats(
    val grossWPM: Double,
    val netWPM: Double,
    val accuracy: Double,
    val adjustedWPM: Double,
    val totalChars: Int,
    val correctChars: Int,
    val uncorrectedErrors: Int,
    val timeSeconds: Double
)

fun calculateTypingStats(
    totalChars: Int,
    correctChars: Int,
    uncorrectedErrors: Int,
    timeMillis: Long
): TypingStats {
    val timeSeconds = timeMillis / 1000.0
    val minutes = timeSeconds / 60.0
    
    val grossWPM = if (minutes > 0) (totalChars / 5.0) / minutes else 0.0
    val errorsPerMinute = if (minutes > 0) uncorrectedErrors / minutes else 0.0
    val netWPM = grossWPM - errorsPerMinute
    val accuracy = if (totalChars > 0) (correctChars.toDouble() / totalChars) * 100.0 else 100.0
    val adjustedWPM = netWPM * (accuracy / 100.0)
    
    return TypingStats(
        grossWPM = grossWPM,
        netWPM = netWPM,
        accuracy = accuracy,
        adjustedWPM = adjustedWPM,
        totalChars = totalChars,
        correctChars = correctChars,
        uncorrectedErrors = uncorrectedErrors,
        timeSeconds = timeSeconds
    )
} 
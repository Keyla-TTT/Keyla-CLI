package org.keyla.util

/**
 * Formats a double value to a string with exactly 1 decimal place.
 *
 * This function must be implemented by each platform-specific module to provide
 * platform-appropriate number formatting, taking into account locale-specific
 * decimal separators and number formatting conventions.
 *
 * @param value The double value to format
 * @return A string representation of the value with exactly 1 decimal place
 */
expect fun format1f(value: Double): String

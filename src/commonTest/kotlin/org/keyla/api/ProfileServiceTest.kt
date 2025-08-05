package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class ProfileServiceTest {
    @Test
    fun `ProfileListResponse should have correct structure`() {
        val response =
            ProfileListResponse(
                profiles =
                    listOf(
                        ProfileResponse(
                            id = "profile1",
                            name = "John Doe",
                            email = "john@example.com",
                            settings = setOf("dark_mode", "sound_enabled"),
                        ),
                        ProfileResponse(
                            id = "profile2",
                            name = "Jane Smith",
                            email = "jane@example.com",
                            settings = setOf("light_mode"),
                        ),
                    ),
            )

        assertEquals(2, response.profiles.size)
        assertEquals("profile1", response.profiles[0].id)
        assertEquals("John Doe", response.profiles[0].name)
        assertEquals("john@example.com", response.profiles[0].email)
        assertEquals(setOf("dark_mode", "sound_enabled"), response.profiles[0].settings)
        assertEquals("profile2", response.profiles[1].id)
        assertEquals("Jane Smith", response.profiles[1].name)
    }

    @Test
    fun `CreateProfileRequest should have correct structure`() {
        val request =
            CreateProfileRequest(
                name = "New User",
                email = "newuser@example.com",
                settings = setOf("dark_mode", "sound_enabled", "auto_save"),
            )

        assertEquals("New User", request.name)
        assertEquals("newuser@example.com", request.email)
        assertEquals(setOf("dark_mode", "sound_enabled", "auto_save"), request.settings)
    }

    @Test
    fun `ProfileResponse should have correct structure`() {
        val profile =
            ProfileResponse(
                id = "profile123",
                name = "Test User",
                email = "test@example.com",
                settings = setOf("dark_mode", "sound_enabled"),
            )

        assertEquals("profile123", profile.id)
        assertEquals("Test User", profile.name)
        assertEquals("test@example.com", profile.email)
        assertEquals(setOf("dark_mode", "sound_enabled"), profile.settings)
    }

    @Test
    fun `CreateProfileRequest with empty settings should work`() {
        val request =
            CreateProfileRequest(
                name = "Minimal User",
                email = "minimal@example.com",
            )

        assertEquals("Minimal User", request.name)
        assertEquals("minimal@example.com", request.email)
        assertTrue(request.settings.isEmpty())
    }
}

package model.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class UserDTOTest {
    @Test
    fun userDTO_serializationDeserialization() {
        val user = User(1, "Name", "SecondName", "email",
            birthdayDate = Date(1L), creationDate = Date(2L), deletionDate = Date(3L))
        val processedUser = ObjectMapper()
            .readerFor(User::class.java)
            .readValue<User>(ObjectMapper().writeValueAsString(user))
        assertEquals(processedUser, user)
    }
}
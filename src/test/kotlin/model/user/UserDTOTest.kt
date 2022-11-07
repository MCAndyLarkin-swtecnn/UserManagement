package model.user

import com.fasterxml.jackson.databind.ObjectMapper
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class UserDTOTest {
    @Test
    fun userDTO_serializationDeserialization() {
        val user = User(1, "Name", "SecondName", "email",
            birthdayDate = 1L, creationDate = 2L, deletionDate = 3L)
        val processedUser = ObjectMapper()
            .readerFor(User::class.java)
            .readValue<User>(ObjectMapper().writeValueAsString(user))
        assertEquals(processedUser, user)
    }

    @Test
    fun newUserDTO_serializationDeserialization() {
        val user = NewUser("Name", "SecondName", "email",
            birthdayDate = 1L)
        val processedUser = ObjectMapper()
            .readerFor(NewUser::class.java)
            .readValue<NewUser>(ObjectMapper().writeValueAsString(user))
        assertEquals(processedUser, user)
    }

    @Test
    fun updateUserDTO_serializationDeserialization() {
        val user = UpdateUser(1, "Name", "SecondName", "email",
            birthdayDate = 1L)
        val processedUser = ObjectMapper()
            .readerFor(UpdateUser::class.java)
            .readValue<UpdateUser>(ObjectMapper().writeValueAsString(user))
        assertEquals(processedUser, user)
    }
}
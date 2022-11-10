package model.user

import com.fasterxml.jackson.databind.ObjectMapper
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import gateway.model.user.UserContract
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UserDTOTest {
    @Test
    fun userDTO_serialization() {
        val user = TestUserModelsUtils.getTestUser()
        with(ObjectMapper()) {
            assertEquals(readTree(writeValueAsString(user)), readTree(user.mapToJsonManually()))
        }
    }

    @Test
    fun userDTO_deserialization() {
        val user = TestUserModelsUtils.getTestUser()
        with(ObjectMapper()) {
            assertEquals(readerFor(User::class.java).readValue<User>(user.mapToJsonManually()), user)
        }
    }

    @Test
    fun newUserDTO_serialization() {
        val user = TestUserModelsUtils.getTestNewUser()
        with(ObjectMapper()) {
            assertEquals(readTree(writeValueAsString(user)), readTree(user.mapToJsonManually()))
        }
    }

    @Test
    fun newUserDTO_deserialization() {
        val user = TestUserModelsUtils.getTestNewUser()
        with(ObjectMapper()) {
            assertEquals(readerFor(NewUser::class.java).readValue<User>(user.mapToJsonManually()), user)
        }
    }

    @Test
    fun updatedUserDTO_serialization() {
        val user = TestUserModelsUtils.getTestUpdatedUser()
        with(ObjectMapper()) {
            assertEquals(readTree(writeValueAsString(user)), readTree(user.mapToJsonManually()))
        }
    }

    @Test
    fun updatedUserDTO_deserialization() {
        val user = TestUserModelsUtils.getTestUpdatedUser()
        with(ObjectMapper()) {
            assertEquals(readerFor(UpdateUser::class.java).readValue<User>(user.mapToJsonManually()), user)
        }
    }

    companion object {
        private fun User.mapToJsonManually() = """{
                "${UserContract.ID}": $id,
                "${UserContract.FIRST_NAME}": "$firstName",
                "${UserContract.SECOND_NAME}": "$secondName",
                "${UserContract.EMAIL}": "$email",
                "${UserContract.BIRTH_DATE}": $birthdayDate,
                "${UserContract.CREATION_DATE}": $creationDate,
                "${UserContract.DELETION_DATE}": $deletionDate
            }"""

        private fun NewUser.mapToJsonManually() = """{
                "${UserContract.FIRST_NAME}": "$firstName",
                "${UserContract.SECOND_NAME}": "$secondName",
                "${UserContract.EMAIL}": "$email",
                "${UserContract.BIRTH_DATE}": $birthdayDate
            }"""

        private fun UpdateUser.mapToJsonManually() = """{
                "${UserContract.ID}": $id,
                "${UserContract.FIRST_NAME}": "$firstName",
                "${UserContract.SECOND_NAME}": "$secondName",
                "${UserContract.EMAIL}": "$email",
                "${UserContract.BIRTH_DATE}": $birthdayDate
            }"""
    }
}
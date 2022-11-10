package model.user

import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import kotlin.random.Random

object TestUserModelsUtils {
    fun getTestUser(): User {
        val randomId = Random.nextInt()
        return User(
            id = randomId,
            "FirstName$randomId",
            "SecondName$randomId",
            "email$randomId",
            birthdayDate = Random.nextLong(),
            creationDate = Random.nextLong(),
            deletionDate = Random.nextLong()
        )
    }

    fun getTestNewUser(): NewUser {
        val randomId = Random.nextInt()
        return NewUser(
            "FirstName$randomId",
            "SecondName$randomId",
            "email$randomId",
            birthdayDate = Random.nextLong()
        )
    }

    fun getTestUpdatedUser(): UpdateUser {
        val randomId = Random.nextInt()
        return UpdateUser(
            id = randomId,
            "FirstName$randomId",
            "SecondName$randomId",
            "email$randomId",
            birthdayDate = Random.nextLong()
        )
    }

    fun User.toNewUser() = NewUser(firstName, secondName, email, birthdayDate)

    fun User.toUpdateUser() = UpdateUser(this.id, firstName, secondName, email, birthdayDate)
}
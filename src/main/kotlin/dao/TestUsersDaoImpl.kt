package dao

import model.user.User
import model.user.UserContract
import entities.ListingParams
import java.util.*
import kotlin.NoSuchElementException

class TestUsersDaoImpl : UsersDao {
    private val users: MutableList<User> = mutableListOf(
        User("0",
            "Maxim",
            "Sokolov",
        "mcal@mcal.mcal",
        Date(2000,4,23),
            Date(2022,10,31),
            null,
        ),
        User("1",
            "NeMaxim",
            "NeSokolov",
            "nemcal@mcal.mcal",
            Date(2004,1,23),
            Date(2022,10,31),
            Date(2022,10,31),
        )
    )
    override fun getAllUsers(params: ListingParams): List<User> = users.let { users -> if (params.showActive) {
            users.filter { user ->  user.deletionDate == null} .toMutableList()
        } else users
    }.apply {
        params.sortBy?.let {
            when (params.sortBy) {
                UserContract.ID -> sortBy{ user: User -> user.id }
                UserContract.FIRST_NAME -> sortBy{ user: User -> user.firstName }
                UserContract.SECOND_NAME -> sortBy{ user: User -> user.secondName }
                UserContract.EMAIL -> sortBy{ user: User -> user.email }
                UserContract.BIRTH_DATE -> sortBy{ user: User -> user.birthDate }
            }
        }
        if (!params.sortOrder) reverse()
        params.offset?.let {
            subList(params.offset, size)
        }
        params.limit?.let {
            subList(0, params.limit)
        }
    }

    override fun getUserById(id: String): User = users.first { user -> id == user.id }

    override fun deleteUserById(id: String): User = users.first { user -> id == user.id }
        .apply { if (deletionDate == null) deletionDate = Calendar.getInstance().time else throw IllegalStateException() }

    override fun addUser(user: User): User = user.also { user -> users.add(user)}

    override fun updateUser(user: User): User = users.indexOfFirst { foundUser -> foundUser.id == user.id }
        .let { index ->
            if (index < 0) {
                throw NoSuchElementException(user.toString())
            }
            users.add(index, user)
            return@let user
        }
}
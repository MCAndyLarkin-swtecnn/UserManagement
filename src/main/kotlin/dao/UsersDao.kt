package dao

import model.user.User
import entities.ListingParams

interface UsersDao {
    fun getAllUsers(params: ListingParams): List<User>
    fun getUserById(id: String): User
    fun deleteUserById(id: String): User
    fun addUser(user: User): User
    fun updateUser(user: User): User
}

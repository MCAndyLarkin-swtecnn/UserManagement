package datasource.dao

import model.user.User
import entities.ListingParams

interface UsersDao {
    fun getAllUsers(params: ListingParams): List<User>
    fun getUserById(id: Int): User
    fun deleteUserById(id: Int): User
    fun addUser(user: User): User
    fun updateUser(user: User): User
}

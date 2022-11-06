package datasource.dao

import datasource.dao.model.user.UserDBModel
import entities.ListingParams

interface UsersDao {
    fun getAllUsers(params: ListingParams): List<UserDBModel>
    fun getUserById(id: Int): UserDBModel?
    fun getUserByEmail(email: String): UserDBModel?
    fun deleteUserById(id: Int): UserDBModel?
    fun addUser(user: UserDBModel): UserDBModel?
    fun updateUser(user: UserDBModel): UserDBModel?
}

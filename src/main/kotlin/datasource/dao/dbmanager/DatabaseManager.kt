package datasource.dao.dbmanager

import datasource.dao.model.user.UserDBModel
import entities.ListingParams

interface DatabaseManager {
    fun getAllUsers(params: ListingParams): List<UserDBModel>
    fun getUserById(id: Int): UserDBModel?
    fun deleteUserById(id: Int): UserDBModel?
    fun getUserByEmail(email: String): UserDBModel?
    fun addUser(user: UserDBModel): UserDBModel?
    fun updateUser(user: UserDBModel): UserDBModel?
}
package service

import model.user.User
import entities.ListingParams

interface UserManagementService {
    fun getAllUsers(params: ListingParams): Result<List<User>>
    fun getUserById(id: String): Result<User>
    fun deleteUserById(id: String): Result<User>
    fun addUser(user: User): Result<User>
    fun updateUser(user: User): Result<User>
}
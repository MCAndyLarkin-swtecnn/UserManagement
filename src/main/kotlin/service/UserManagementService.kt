package service

import entities.ListingParams
import model.user.*

interface UserManagementService {
    fun getAllUsers(params: ListingParams): Result<List<User>>
    fun getUserById(id: Int): Result<User>
    fun deleteUserById(id: Int): Result<User>
    fun addUser(user: NewUser): Result<User>
    fun updateUser(updatedUser: UpdateUser): Result<User>
}

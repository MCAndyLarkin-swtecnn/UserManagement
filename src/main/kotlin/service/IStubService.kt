package service

import entities.ListingParams
import model.user.NewUser
import model.user.UpdateUser

//TODO: Demo service. Should be deleted! UserManagementService should be implemented and used instead.
interface IStubService {
    fun getAllUsers(params: ListingParams): Result<String>
    fun getUserById(id: Int): Result<String>
    fun deleteUserById(id: Int): Result<String>
    fun addUser(user: NewUser): Result<String>
    fun updateUser(updatedUser: UpdateUser): Result<String>
}
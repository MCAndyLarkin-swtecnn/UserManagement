package service

import entities.ListingParams
import model.user.NewUser
import model.user.UpdateUser

//TODO: Demo service. Should be deleted! UserManagementService should be implemented and used instead.
class StubService : IStubService {
    override fun getAllUsers(params: ListingParams): Result<String> =
        Result.success("getAllUsers(params: $params)")

    override fun getUserById(id: Int): Result<String> =
        Result.success("getUserById(id: $id)")

    override fun deleteUserById(id: Int): Result<String> =
        Result.success("deleteUserById(id: $id)")

    override fun addUser(user: NewUser): Result<String> =
        Result.success("addUser(user: $user)")

    override fun updateUser(updatedUser: UpdateUser): Result<String> =
        Result.success("updateUser(updatedUser: $updatedUser)")
}
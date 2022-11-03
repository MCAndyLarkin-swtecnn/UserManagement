package service

import datasource.dao.UsersDao
import entities.ListingParams
import model.user.NewUser
import model.user.UpdateUser
import model.user.User
import utils.ResultUtils
import validation.UsersManagementValidator
import java.util.*

class UserManagementServiceImpl(private val dao: UsersDao,
                                private val validator: UsersManagementValidator) : UserManagementService {

    override fun getAllUsers(params: ListingParams): Result<List<User>> = ResultUtils.resultFromTryCatch {
        validator.checkListingParamsValidity(params)
        dao.getAllUsers(params)
    }

    override fun getUserById(id: Int): Result<User> = ResultUtils.resultFromTryCatch {
        dao.getUserById(id)
    }

    override fun deleteUserById(id: Int): Result<User> = ResultUtils.resultFromTryCatch {
        dao.getUserById(id).also { user ->
            with(user) {
                dao.updateUser(User(id, firstName, secondName, email, birthdayDate,
                        creationDate, Date()))
        } }
    }

    override fun addUser(user: NewUser): Result<User> = ResultUtils.resultFromTryCatch {
        validator.checkNewUserValidity(user)
        //TODO: Check email uniqueness
        with(user) {
            dao.addUser(User(null, firstName, secondName, email, birthdayDate, Date(), null))
        }
    }

    override fun updateUser(updatedUser: UpdateUser): Result<User> =  ResultUtils.resultFromTryCatch {
        validator.checkUpdateUserValidity(updatedUser)
        //TODO: Check email uniqueness
        dao.getUserById(updatedUser.id)
            .also { existingUser -> with(updatedUser) {
                dao.updateUser(User(id, firstName, secondName, email, birthdayDate,
                    existingUser.creationDate, existingUser.deletionDate)) }
            }
    }
}
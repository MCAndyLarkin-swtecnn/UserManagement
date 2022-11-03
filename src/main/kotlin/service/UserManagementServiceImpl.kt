package service

import datasource.dao.UsersDao
import entities.ListingParams
import exceptions.EmailAlreadyUsedException
import exceptions.UserAdditionInterruptedException
import exceptions.UserUpdatingInterruptedException
import exceptions.UserWithIdNotFoundException
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
        dao.getUserById(id) ?: throw UserWithIdNotFoundException(id)
    }

    override fun deleteUserById(id: Int): Result<User> = ResultUtils.resultFromTryCatch {
        dao.getUserById(id)?.also { user ->
            with(user) {
                dao.updateUser(User(id, firstName, secondName, email, birthdayDate,
                        creationDate, Date()))
        } }  ?: throw UserWithIdNotFoundException(id)
    }

    override fun addUser(user: NewUser): Result<User> = ResultUtils.resultFromTryCatch {
        validator.checkNewUserValidity(user)
        val sameEmailUser = dao.getUserByEmail(user.email)
        if (sameEmailUser == null) {
            with(user) {
                dao.addUser(User(null, firstName, secondName, email, birthdayDate, Date(), null))
                    ?: throw UserAdditionInterruptedException(user)
            }
        } else throw EmailAlreadyUsedException(user.email)
    }

    override fun updateUser(updatedUser: UpdateUser): Result<User> =  ResultUtils.resultFromTryCatch {
        validator.checkUpdateUserValidity(updatedUser)
        val sameEmailUser = dao.getUserByEmail(updatedUser.email)
        val sameIdUser = dao.getUserById(updatedUser.id)
        sameIdUser?.let {
            if (sameEmailUser == null || sameEmailUser.id == sameIdUser.id) {
                with(updatedUser) {
                    dao.updateUser(User(id, firstName, secondName, email, birthdayDate,
                        sameIdUser.creationDate, sameIdUser.deletionDate))
                        ?: throw UserUpdatingInterruptedException(updatedUser)
                }
            } else {
                throw EmailAlreadyUsedException(updatedUser.email)
            }
        } ?: throw UserWithIdNotFoundException(updatedUser.id)
    }
}
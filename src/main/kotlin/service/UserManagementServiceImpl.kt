package service

import dao.UsersDao
import model.user.User
import entities.ListingParams
import exceptions.PassedUserInvalidException
import utils.ResultUtils
import utils.ValidationUtility

class UserManagementServiceImpl(private val usersDao: UsersDao,
                                private val validator: ValidationUtility) : UserManagementService {
    override fun getAllUsers(params: ListingParams): Result<List<User>> {
        return ResultUtils.createResultFrom { usersDao.getAllUsers(params) }
    }

    override fun getUserById(id: String): Result<User> {
        return ResultUtils.createResultFrom { usersDao.getUserById(id) }
    }

    override fun deleteUserById(id: String): Result<User> {
        return ResultUtils.createResultFrom { usersDao.deleteUserById(id) }
    }

    override fun addUser(user: User): Result<User> = ResultUtils.createResultFrom {
        if (user.let(validator::verifyUser)) {
            usersDao.addUser(user)
        } else throw PassedUserInvalidException(user)
    }

    override fun updateUser(user: User): Result<User> = ResultUtils.createResultFrom {
        if (user.let(validator::verifyUser)) {
            usersDao.addUser(user)
        } else throw PassedUserInvalidException(user)
    }
}
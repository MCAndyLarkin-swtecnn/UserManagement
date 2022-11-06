package service

import datasource.dao.UsersDao
import datasource.dao.model.user.UserDBModel
import entities.ListingParams
import exceptions.EmailAlreadyUsedException
import exceptions.UserAdditionInterruptedException
import exceptions.UserUpdatingInterruptedException
import exceptions.UserWithIdNotFoundException
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import mapper.GatewayDbUserMapper
import utils.ResultUtils
import validation.UsersManagementValidator
import java.util.*

class UserManagementServiceImpl(private val dao: UsersDao,
                                private val validator: UsersManagementValidator,
                                private val gatewayDbUserMapper: GatewayDbUserMapper) : UserManagementService {

    override fun getAllUsers(params: ListingParams): Result<List<User>> = ResultUtils.resultFromTryCatch {
        validator.checkListingParamsValidity(params)
        dao.getAllUsers(params).map(gatewayDbUserMapper::mapDBModelToUser)
    }

    override fun getUserById(id: Int): Result<User> = ResultUtils.resultFromTryCatch {
        dao.getUserById(id)?.let(gatewayDbUserMapper::mapDBModelToUser) ?: throw UserWithIdNotFoundException(id)
    }

    override fun deleteUserById(id: Int): Result<User> = ResultUtils.resultFromTryCatch {
        dao.getUserById(id)?.also { user ->
            dao.updateUser(with(user) { UserDBModel(id, firstName, secondName, email, birthdayDate, creationDate, Date()) })
        }?.let(gatewayDbUserMapper::mapDBModelToUser) ?: throw UserWithIdNotFoundException(id)
    }

    override fun addUser(user: NewUser): Result<User> = ResultUtils.resultFromTryCatch {
        validator.checkNewUserValidity(user)
        val sameEmailUser = dao.getUserByEmail(user.email)
        if (sameEmailUser == null) {
            dao.addUser(gatewayDbUserMapper.mapNewUserToDBModel(user, null))
                ?.let(gatewayDbUserMapper::mapDBModelToUser)
                ?: throw UserAdditionInterruptedException(user)
        } else throw EmailAlreadyUsedException(user.email)
    }

    override fun updateUser(updatedUser: UpdateUser): Result<User> =  ResultUtils.resultFromTryCatch {
        validator.checkUpdateUserValidity(updatedUser)
        val sameEmailUser = dao.getUserByEmail(updatedUser.email)
        val sameIdUser = dao.getUserById(updatedUser.id)
        sameIdUser?.let {
            if (sameEmailUser == null || sameEmailUser.id == sameIdUser.id) {
                dao.updateUser(gatewayDbUserMapper.mapUpdateUserToDBModel(updatedUser, Date(), null))
                    ?.let(gatewayDbUserMapper::mapDBModelToUser)
                    ?: throw UserUpdatingInterruptedException(updatedUser)
            } else {
                throw EmailAlreadyUsedException(updatedUser.email)
            }
        } ?: throw UserWithIdNotFoundException(updatedUser.id)
    }
}
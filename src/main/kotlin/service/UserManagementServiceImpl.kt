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
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

class UserManagementServiceImpl(private val dao: UsersDao,
                                private val gatewayDbUserMapper: GatewayDbUserMapper) : UserManagementService {

    override fun getAllUsers(params: ListingParams): Result<List<User>> = runCatching {
        dao.getAllUsers(params).map(gatewayDbUserMapper::mapDBModelToUser)
    }

    override fun getUserById(id: Int): Result<User> = runCatching {
        dao.getUserById(id)?.let(gatewayDbUserMapper::mapDBModelToUser) ?: throw UserWithIdNotFoundException(id)
    }

    override fun deleteUserById(id: Int): Result<User> = runCatching {
        dao.getUserById(id)?.also { user ->
            dao.updateUser(with(user) { UserDBModel(id, firstName, secondName, email, birthdayDate, creationDate, Date()) })
        }?.let(gatewayDbUserMapper::mapDBModelToUser) ?: throw UserWithIdNotFoundException(id)
    }

    override fun addUser(user: NewUser): Result<User> = runCatching {
        try {
            dao.addUser(gatewayDbUserMapper.mapNewUserToDBModel(user, null))
                ?.let(gatewayDbUserMapper::mapDBModelToUser)
                ?: throw UserAdditionInterruptedException(user)
        } catch (ex: UnableToExecuteStatementException) {
            throw EmailAlreadyUsedException(user.email)
        }
    }

    override fun updateUser(updatedUser: UpdateUser): Result<User> = runCatching {
        val sameIdUser = dao.getUserById(updatedUser.id)
        sameIdUser?.let {
            try {
                dao.updateUser(gatewayDbUserMapper.mapUpdateUserToDBModel(updatedUser, Date(), null))
                    ?.let(gatewayDbUserMapper::mapDBModelToUser)
                    ?: throw UserUpdatingInterruptedException(updatedUser)
            } catch (ex: UnableToExecuteStatementException) {
                throw EmailAlreadyUsedException(updatedUser.email)
            }
        } ?: throw UserWithIdNotFoundException(updatedUser.id)
    }
}
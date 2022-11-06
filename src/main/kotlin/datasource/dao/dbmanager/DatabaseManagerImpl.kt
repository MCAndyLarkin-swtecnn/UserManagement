package datasource.dao.dbmanager

import datasource.dao.UsersMappedDao
import datasource.dao.model.user.UserDBModel
import entities.ListingParams
import org.jdbi.v3.core.Jdbi

class DatabaseManagerImpl(private val jdbi: Jdbi) : DatabaseManager {
    override fun getAllUsers(params: ListingParams): List<UserDBModel> =
        jdbi.onDemand(UsersMappedDao::class.java).getAllUsers(params);


    override fun getUserById(id: Int): UserDBModel? =
        jdbi.onDemand(UsersMappedDao::class.java).getUserById(id)

    override fun deleteUserById(id: Int): UserDBModel? =
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.getUserById(id)?.also {
                dao.deleteUserById(id)
            }
        }

    override fun getUserByEmail(email: String): UserDBModel? =
        jdbi.onDemand(UsersMappedDao::class.java).getUserByEmail(email)

    override fun addUser(user: UserDBModel): UserDBModel? =
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.insertUser(user)
                ?.let(dao::getUserById)
        }

    override fun updateUser(user: UserDBModel): UserDBModel? =
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.updateUser(user)
                //Why it is not actual ???
                ?.let(dao::getUserById)
        }
}
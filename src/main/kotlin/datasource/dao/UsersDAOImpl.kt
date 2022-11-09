package datasource.dao

import datasource.dao.model.user.UserDBModel
import datasource.dao.validation.DaoValidator
import entities.ListingParams
import org.jdbi.v3.core.Jdbi


//TODO: Catch SQL EXCEPTIONS
class UsersDAOImpl(jdbi: Jdbi,
                   private val validator: DaoValidator) : UsersDao {
    private val mappedDao: UsersMappedDao by UsersMappedDao.Delegate(jdbi)

    init {
        mappedDao.initTable()
    }

    override fun getAllUsers(params: ListingParams?): List<UserDBModel> =
        //TODO: Add ListingParams validation
        mappedDao.let { dao ->
            params?.let(dao::getAllUsers) ?: dao.getAllUsers()
        }

    override fun getUserById(id: Int): UserDBModel? =
        mappedDao.getUserById(id)

    override fun getUserByEmail(email: String): UserDBModel? =
        mappedDao.getUserByEmail(email)

    override fun addUser(user: UserDBModel): UserDBModel? = validator.checkUserValidity(user).run {
        mappedDao.let { dao ->
            dao.insertUser(user)
                ?.let(dao::getUserById)
        }
    }

    override fun updateUser(user: UserDBModel): UserDBModel? = validator.checkUserValidity(user).run {
        mappedDao.let { dao ->
            dao.updateUser(user)
                //Why it is not actual ???
                ?.let(dao::getUserById)
        }
    }
}
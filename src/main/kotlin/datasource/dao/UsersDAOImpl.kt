package datasource.dao

import datasource.dao.validation.DaoValidator
import entities.ListingParams
import model.user.User
import org.jdbi.v3.core.Jdbi


//TODO: Catch SQL EXCEPTIONS
class UsersDAOImpl(private val jdbi: Jdbi,
                   private val validator: DaoValidator) : UsersDao {

    override fun getAllUsers(params: ListingParams): List<User> =
        //TODO: Add ListingParams validation
        jdbi.onDemand(UsersMappedDao::class.java).getAllUsers(params);

    override fun getUserById(id: Int): User? =
        jdbi.onDemand(UsersMappedDao::class.java).getUserById(id)


    override fun getUserByEmail(email: String): User? =
        jdbi.onDemand(UsersMappedDao::class.java).getUserByEmail(email)

    override fun deleteUserById(id: Int): User? =
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.getUserById(id)?.also {
                dao.deleteUserById(id)
            }
        }

    override fun addUser(user: User): User? = validator.checkUserValidity(user).run {
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.insertUser(user)
                ?.let(dao::getUserById)
        }
    }

    override fun updateUser(user: User): User? = validator.checkUserValidity(user).run {
        jdbi.onDemand(UsersMappedDao::class.java).let { dao ->
            dao.updateUser(user)
                //Why it is not actual ???
                ?.let(dao::getUserById)
        }
    }
}
package datasource.dao

import datasource.dao.dbmanager.DatabaseManager
import datasource.dao.model.user.UserDBModel
import datasource.dao.validation.DaoValidator
import entities.ListingParams


//TODO: Catch SQL EXCEPTIONS
class UsersDAOImpl(private val dbManager: DatabaseManager,
                   private val validator: DaoValidator) : UsersDao {

    override fun getAllUsers(params: ListingParams): List<UserDBModel> =
        //TODO: Add ListingParams validation
        dbManager.getAllUsers(params);

    override fun getUserById(id: Int): UserDBModel? =
        dbManager.getUserById(id)

    override fun getUserByEmail(email: String): UserDBModel? =
        dbManager.getUserByEmail(email)

    override fun deleteUserById(id: Int): UserDBModel? =
        dbManager.deleteUserById(id)

    override fun addUser(user: UserDBModel): UserDBModel? = validator.checkUserValidity(user).run {
        dbManager.addUser(user)
    }

    override fun updateUser(user: UserDBModel): UserDBModel? = validator.checkUserValidity(user).run {
        dbManager.updateUser(user)
    }
}
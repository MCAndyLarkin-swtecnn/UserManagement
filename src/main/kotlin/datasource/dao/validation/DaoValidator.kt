package datasource.dao.validation

import model.user.User


interface DaoValidator {
    fun checkUserValidity(user: User)
}
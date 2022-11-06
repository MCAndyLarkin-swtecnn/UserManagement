package datasource.dao.validation

import datasource.dao.model.user.UserDBModel


interface DaoValidator {
    fun checkUserValidity(user: UserDBModel)
}
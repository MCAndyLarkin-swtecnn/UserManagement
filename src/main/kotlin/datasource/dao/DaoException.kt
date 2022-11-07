package datasource.dao

import exceptions.UserManagementException

sealed class DaoException(message: String) : UserManagementException(message)

class UnexpectedSQLInjectionException(injectionArea: String)
    : DaoException("The provided value contains SQL injection/pattern which may break database integrity. \n$injectionArea")

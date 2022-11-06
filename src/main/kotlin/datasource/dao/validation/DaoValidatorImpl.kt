package datasource.dao.validation

import com.github.rkpunjal.sqlsafe.SqlSafeUtil
import datasource.dao.UnexpectedSQLInjectionException
import datasource.dao.model.user.UserDBModel


class DaoValidatorImpl : DaoValidator{
    override fun checkUserValidity(user: UserDBModel) {
        if (user.firstName.isSqlInjection()) {
            throw UnexpectedSQLInjectionException("${buildUserInvalidWarningMessage(user)} : " +
                    buildSqlInjectionWarningMessage("User::firstName -- ${user.firstName}")
            )
        }
        if (user.secondName.isSqlInjection()) {
            throw UnexpectedSQLInjectionException("${buildUserInvalidWarningMessage(user)} : " +
                    buildSqlInjectionWarningMessage("User::secondName -- ${user.secondName}")
            )
        }
        if (user.email.isSqlInjection()) {
            throw UnexpectedSQLInjectionException("${buildUserInvalidWarningMessage(user)} : " +
                    buildSqlInjectionWarningMessage("User::email -- ${user.email}")
            )
        }
    }

    private fun String.isSqlInjection(): Boolean = !SqlSafeUtil.isSqlInjectionSafe(this)

    companion object {
        private const val SQL_INJECTION_WARNING_PATTERN = "Field {} contains SQL injection!"
        private const val INVALID_USER_WARNING_PATTERN = "The user ({}) is invalid"

        private fun buildSqlInjectionWarningMessage(wrongField: String) = SQL_INJECTION_WARNING_PATTERN.format(wrongField)

        private fun buildUserInvalidWarningMessage(user: UserDBModel) = INVALID_USER_WARNING_PATTERN.format(user)
    }
}
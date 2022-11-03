package datasource.dao

import entities.ListingParams
import model.user.User
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.ResultSet


@RegisterRowMapper(UsersMappedDao.UserMapper::class)
interface UsersMappedDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS $USERS_TABLE_NAME " +
            "($USER_ID_COLUMN INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
            "$USER_FIRST_NAME_COLUMN VARCHAR, " +
            "$USER_SECOND_NAME_COLUMN VARCHAR, " +
            "$USER_EMAIL_COLUMN VARCHAR, " +
            "$USER_BIRTHDAY_DATE_COLUMN DATE, " +
            "$USER_CREATION_DATE_COLUMN DATE, " +
            "$USER_DELETION_DATE_COLUMN DATE)")
    fun initTable()

    @SqlUpdate("INSERT INTO $USERS_TABLE_NAME " +
            "($USER_FIRST_NAME_COLUMN, $USER_SECOND_NAME_COLUMN, $USER_EMAIL_COLUMN, " +
            "$USER_BIRTHDAY_DATE_COLUMN, $USER_CREATION_DATE_COLUMN, $USER_DELETION_DATE_COLUMN) " +
            "VALUES (:user.firstName, :user.secondName, :user.email, :user.birthdayDate, :user.creationDate, :user.deletionDate)")
    @GetGeneratedKeys(USER_ID_COLUMN)
    fun insertUser(@BindBean("user") user: User): Int

    @SqlUpdate("UPDATE $USERS_TABLE_NAME " +
            "SET $USER_FIRST_NAME_COLUMN = :user.firstName, " +
            "$USER_SECOND_NAME_COLUMN = :user.secondName, " +
            "$USER_EMAIL_COLUMN = :user.email, " +
            "$USER_BIRTHDAY_DATE_COLUMN = :user.birthdayDate, " +
            "$USER_CREATION_DATE_COLUMN = :user.creationDate, " +
            "$USER_DELETION_DATE_COLUMN = :user.deletionDate " +
            "WHERE $USER_ID_COLUMN = :user.id")
    @GetGeneratedKeys(USER_ID_COLUMN)
    fun updateUser(@BindBean("user") user: User): Int

    @SqlQuery("SELECT * from $USERS_TABLE_NAME LIMIT :params.limit OFFSET :params.offset")
    fun getAllUsers(@BindBean("params") params: ListingParams): List<User>

    @SqlQuery("SELECT * from $USERS_TABLE_NAME where $USER_ID_COLUMN = ?")
    fun getUserById(id: Int): User

    @SqlQuery("DELETE from $USERS_TABLE_NAME where $USER_ID_COLUMN = ?")
    fun deleteUserById(id: Int): Number


    class UserMapper : RowMapper<User> {
        override fun map(row: ResultSet, ctx: StatementContext?): User =
            User(row.getInt(USER_ID_COLUMN),
                row.getString(USER_FIRST_NAME_COLUMN),
                row.getString(USER_SECOND_NAME_COLUMN),
                row.getString(USER_EMAIL_COLUMN),
                row.getDate(USER_BIRTHDAY_DATE_COLUMN),
                row.getDate(USER_CREATION_DATE_COLUMN),
                row.getDate(USER_DELETION_DATE_COLUMN)
            )
    }

    companion object {
        const val USERS_TABLE_NAME = "Users"

        const val USER_ID_COLUMN = "id"
        const val USER_FIRST_NAME_COLUMN = "firstName"
        const val USER_SECOND_NAME_COLUMN = "secondName"
        const val USER_EMAIL_COLUMN = "email"
        const val USER_BIRTHDAY_DATE_COLUMN = "birthdayDate"
        const val USER_CREATION_DATE_COLUMN = "creationDate"
        const val USER_DELETION_DATE_COLUMN = "deletionDate"
    }
}
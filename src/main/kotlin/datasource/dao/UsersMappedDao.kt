package datasource.dao

import datasource.dao.model.user.UserDBModel
import entities.ListingParams
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
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
            "$USER_EMAIL_COLUMN VARCHAR UNIQUE, " +
            "$USER_BIRTHDAY_DATE_COLUMN DATE, " +
            "$USER_CREATION_DATE_COLUMN DATE, " +
            "$USER_DELETION_DATE_COLUMN DATE DEFAULT(NULL))")
    fun initTable()

    @SqlUpdate("INSERT INTO $USERS_TABLE_NAME " +
            "($USER_FIRST_NAME_COLUMN, $USER_SECOND_NAME_COLUMN, $USER_EMAIL_COLUMN, " +
            "$USER_BIRTHDAY_DATE_COLUMN, $USER_CREATION_DATE_COLUMN) " +
            "VALUES (:user.firstName, :user.secondName, :user.email, :user.birthdayDate, :user.creationDate)")
    @GetGeneratedKeys(USER_ID_COLUMN)
    fun insertUser(@BindBean("user") user: UserDBModel): Int?

    @SqlUpdate("UPDATE $USERS_TABLE_NAME " +
            "SET $USER_FIRST_NAME_COLUMN = :user.firstName, " +
            "$USER_SECOND_NAME_COLUMN = :user.secondName, " +
            "$USER_EMAIL_COLUMN = :user.email, " +
            "$USER_BIRTHDAY_DATE_COLUMN = :user.birthdayDate, " +
            "$USER_CREATION_DATE_COLUMN = :user.creationDate, " +
            "$USER_DELETION_DATE_COLUMN = :user.deletionDate " +
            "WHERE $USER_ID_COLUMN = :user.id")
    @GetGeneratedKeys(USER_ID_COLUMN)
    fun updateUser(@BindBean("user") user: UserDBModel): Int?

    @SqlQuery("SELECT * from $USERS_TABLE_NAME ORDER BY 'params:sortBy' LIMIT :params.limit OFFSET :params.offset")
    fun getAllUsers(@BindBean("params") params: ListingParams): List<UserDBModel>

    @SqlQuery("SELECT * from $USERS_TABLE_NAME")
    fun getAllUsers(): List<UserDBModel>

    @SqlQuery("SELECT * from $USERS_TABLE_NAME where $USER_ID_COLUMN = ?")
    fun getUserById(id: Int): UserDBModel?

    @SqlQuery("SELECT * from $USERS_TABLE_NAME where $USER_EMAIL_COLUMN = ?")
    @GetGeneratedKeys(USER_ID_COLUMN)
    fun getUserByEmail(email: String): UserDBModel?


    class UserMapper : RowMapper<UserDBModel> {
        override fun map(row: ResultSet, ctx: StatementContext?): UserDBModel =
            UserDBModel(row.getInt(USER_ID_COLUMN),
                row.getString(USER_FIRST_NAME_COLUMN),
                row.getString(USER_SECOND_NAME_COLUMN),
                row.getString(USER_EMAIL_COLUMN),
                row.getDate(USER_BIRTHDAY_DATE_COLUMN),
                row.getDate(USER_CREATION_DATE_COLUMN),
                row.getDate(USER_DELETION_DATE_COLUMN)
            )
    }

    class Delegate(private val jdbi: Jdbi) {
        operator fun getValue(thisRef: Any?, property: Any?): UsersMappedDao =
            jdbi.onDemand(UsersMappedDao::class.java)
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
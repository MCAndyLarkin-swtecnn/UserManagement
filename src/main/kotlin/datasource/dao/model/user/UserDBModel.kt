package datasource.dao.model.user

import java.util.*


data class UserDBModel(
    val id: Int,
    val firstName: String,
    val secondName: String,
    val email: String,
    val birthdayDate: Date,
    val creationDate: Date,
    val deletionDate: Date?) {
    companion object {
        const val UNKNOWN_ID = -1
    }
}

package model.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties("creationDate", "deletionDate")
data class User(
    @JsonProperty(UserContract.ID) val id: String?,
    @JsonProperty(UserContract.FIRST_NAME) val firstName: String,
    @JsonProperty(UserContract.SECOND_NAME) val secondName: String,
    @JsonProperty(UserContract.EMAIL) val email: String,
    @JsonProperty(UserContract.BIRTH_DATE) val birthDate: Date,
    val creationDate: Date? = null,
    /*val*/ var deletionDate: Date? = null
)

object UserContract {
    const val ID ="id"
    const val FIRST_NAME = "firstName"
    const val SECOND_NAME = "secondName"
    const val EMAIL = "email"
    const val BIRTH_DATE = "birthDate"

//    fun validateProperty(property: String?): String? = when (property) {
//        ID, FIRST_NAME, SECOND_NAME, EMAIL, BIRTH_DATE -> property
//        else -> null
//    }
}

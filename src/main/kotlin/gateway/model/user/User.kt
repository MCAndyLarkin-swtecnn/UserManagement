package gateway.model.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class User(
    @JsonProperty(UserContract.ID) val id: Int?,
    @JsonProperty(UserContract.FIRST_NAME) val firstName: String,
    @JsonProperty(UserContract.SECOND_NAME) val secondName: String,
    @JsonProperty(UserContract.EMAIL) val email: String,
    @JsonProperty(UserContract.BIRTH_DATE) val birthdayDate: Long,
    @JsonProperty(UserContract.CREATION_DATE) val creationDate: Long,
    @JsonProperty(UserContract.DELETION_DATE) val deletionDate: Long?
)
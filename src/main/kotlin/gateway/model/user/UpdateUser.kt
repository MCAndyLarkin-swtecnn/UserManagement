package gateway.model.user

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.Max


data class UpdateUser(
    @ApiModelProperty(
        value = "User's unique id.",
        example = "1",
        required = true,
        dataType = "integer"
    )
    @JsonProperty(UserContract.ID)
    val id: Int,

    @ApiModelProperty(
        value = "User's first name.",
        example = "John",
        required = true,
        dataType = "string"
    )
    @Max(100)
    @JsonProperty(UserContract.FIRST_NAME)
    val firstName: String,

    @ApiModelProperty(
        value = "User's second name.",
        example = "Doe",
        required = true,
        dataType = "string"
    )
    @Max(100)
    @JsonProperty(UserContract.SECOND_NAME)
    val secondName: String,

    @ApiModelProperty(
        value = "User's personal email address.",
        example = "username@server.domain",
        required = true,
        dataType = "string"
    )
    @Email
    @JsonProperty(UserContract.EMAIL)
    val email: String,

    @ApiModelProperty(
        value = "User's birthday date.",
        example = "1667662830000",
        required = true,
        dataType = "integer"
    )
    @JsonProperty(UserContract.BIRTH_DATE)
    val birthdayDate: Long
)

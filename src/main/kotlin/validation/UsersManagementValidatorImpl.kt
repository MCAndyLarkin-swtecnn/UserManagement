package validation

import entities.ListingParams
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.UserContract
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator

class UsersManagementValidatorImpl : UsersManagementValidator {
    override fun checkListingParamsValidity(params: ListingParams) {
        params.sortBy?.let { value ->
            checkFieldInUserContract(value, "ListingParams.sortBy in $params")
        }
    }

    override fun checkNewUserValidity(user: NewUser) {
        checkFieldLengthSize(user.firstName, MAX_NAME_LENGTH, "user.firstName")
        checkFieldLengthSize(user.secondName, MAX_SECOND_NAME_LENGTH, "user.secondName")
        checkEmailValidity(user.email)
    }

    override fun checkUpdateUserValidity(user: UpdateUser) {
        checkFieldLengthSize(user.firstName, MAX_NAME_LENGTH, "user.firstName")
        checkFieldLengthSize(user.secondName, MAX_SECOND_NAME_LENGTH, "user.secondName")
        checkEmailValidity(user.email)
    }

    private fun checkFieldInUserContract(field: String, fieldName: String) {
        if (!UserContract.fields.contains(field)) {
            throw InvalidUsersFieldExistException(field, fieldName, UserContract.fields)
        }
    }

    private fun checkFieldLengthSize(value: String, max: Int, valueName: String) {
        if (value.length > max) {
            throw ProvidedFieldTooLongException(valueName, value, max)
        }
    }

    private fun checkEmailValidity(email: String) {
        if (!EmailValidator().isValid(email, null)) {
            throw InvalidEmailException(email)
        }
    }

    companion object {
        private const val MAX_NAME_LENGTH = 100
        private const val MAX_SECOND_NAME_LENGTH = 100
    }
}
package validation

import exceptions.UserManagementException

sealed class ValidationException(message: String) : UserManagementException(message)

class InvalidEmailException(email: String) : ValidationException("The email address '$email' is invalid!")

class ProvidedFieldTooLongException(fieldName: String, field: String, maxSize: Int)
    : ValidationException("The provided field $fieldName='$field' is too long! It should not be greater than $maxSize.")

class InvalidUsersFieldExistException(field: String, fieldName: String, allowedValues: Array<String>)
    : ValidationException("The provided field: $fieldName; '$field' doesn't exists! " +
        "A field's name should be in ${allowedValues.joinToString(prefix = "[", separator = "; ", postfix = "]",)}")


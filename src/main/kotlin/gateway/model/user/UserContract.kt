package gateway.model.user

object UserContract {
    const val ID = "id"
    const val FIRST_NAME = "firstName"
    const val SECOND_NAME = "secondName"
    const val EMAIL = "email"
    const val BIRTH_DATE = "birthdayDate"
    const val CREATION_DATE = "creationDate"
    const val DELETION_DATE = "deletionDate"

    const val ENTRY_REGEX = "$ID|$FIRST_NAME|$SECOND_NAME|$EMAIL|$BIRTH_DATE|$CREATION_DATE|$DELETION_DATE"
}
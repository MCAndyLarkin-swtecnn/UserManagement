package exceptions

import model.user.User

class PassedUserInvalidException(user: User) : Exception("The passed user ($user) is invalid.")

class RequiredParameterMissed(paramName: String) : Exception("The required '$paramName' parameter wasn't passed!")

class RequiredBodyMissed(bodyDescription: String) : Exception("The required '$bodyDescription' entity in body wasn't provided!")

package validation

import entities.ListingParams
import model.user.*

interface UsersManagementValidator {
    fun checkListingParamsValidity(params: ListingParams)
    fun checkNewUserValidity(user: NewUser)
    fun checkUpdateUserValidity(user: UpdateUser)
}
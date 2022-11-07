package validation

import entities.ListingParams
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser

interface UsersManagementValidator {
    fun checkListingParamsValidity(params: ListingParams)
    fun checkNewUserValidity(user: NewUser)
    fun checkUpdateUserValidity(user: UpdateUser)
}
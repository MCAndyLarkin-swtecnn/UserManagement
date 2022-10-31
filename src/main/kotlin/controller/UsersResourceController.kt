package controller

import com.codahale.metrics.annotation.Timed
import entities.ListingParams
import model.user.User
import service.UserManagementService
import model.user.UserContract
import utils.*
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/userManagement")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
class UsersResourceController(private val service: UserManagementService) {

    @Path("/users")
    @GET
    @Timed
    fun getAllUsers(@QueryParam("filterBy") filterBy: Optional<String>,
                    @QueryParam("limit") limit : OptionalInt,
                    @QueryParam("offset") offset : OptionalInt,
                    @QueryParam("sortBy") sortBy : Optional<String>,
                    @QueryParam("sortOrder") sortOrder : Optional<Boolean>,
                    @QueryParam("showActive") showActive : Optional<Boolean>): Response = ResponseUtils.responseFromTryCatch{
        service.getAllUsers(ListingParams(filterBy=OptionalUtils.getOrNull(filterBy), limit = OptionalUtils.getOrNull(limit),
            offset = OptionalUtils.getOrNull(offset), sortBy = OptionalUtils.getOrNull(sortBy),
            sortOrder = OptionalUtils.getOrNull(sortOrder) ?: true, showActive = OptionalUtils.getOrNull(showActive) ?: false))
            .let(ResponseUtils::processResult)

    }

    @Path("user/{id}")
    @GET
    @Timed
    fun getUserById(@PathParam("id") id: Optional<String>): Response = ResponseUtils.responseFromTryCatch{
        RequestValidationUtil.checkRequiredParameter(Pair(UserContract.ID, id))
            .let(service::getUserById)
            .let(ResponseUtils::processResult)
    }

    @Path("/user")
    @DELETE
    @Timed
    fun deleteUserById(@QueryParam("id") id: Optional<String>): Response = ResponseUtils.responseFromTryCatch{
        RequestValidationUtil.checkRequiredParameter(Pair(UserContract.ID, id))
            .let(service::deleteUserById)
            .let(ResponseUtils::processResult)
    }

    @Path("/user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun addNewUser(newUser: Optional<User>): Response = ResponseUtils.responseFromTryCatch{
        RequestValidationUtil.checkRequiredBodyEntity(Pair("User", newUser))
            .let(service::addUser)
            .let(ResponseUtils::processResult)
    }

    @Path("/user")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun updateUser(updatedUser: Optional<User>): Response = ResponseUtils.responseFromTryCatch{
        RequestValidationUtil.checkRequiredBodyEntity(Pair("User", updatedUser))
            .let(service::updateUser)
            .let(ResponseUtils::processResult)
    }
}
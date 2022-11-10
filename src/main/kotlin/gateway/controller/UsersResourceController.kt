package gateway.controller

import com.codahale.metrics.annotation.Timed
import entities.ListingParams
import service.UserManagementService
import gateway.model.user.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.eclipse.jetty.http.HttpStatus
import utils.*
import javax.swing.SortOrder
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path(UsersResourceController.PATH)
@Api(
    value = "/users",
    tags = ["Users"]
)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
class UsersResourceController(private val service: UserManagementService) {

    //valid: http://localhost:8080/users?limit=10&offset=3&sortBy=email&sortOrder=ASC&showActive=true
    @GET
    @ApiOperation(
        value = "Returns all the users",
        response = List::class,
        code = HttpStatus.OK_200,
        notes = "Returns list of existent users"
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    fun getAllUsers(
        @ApiParam(hidden = true)
        @QueryParam(ListingParams.limit)
        @Min(1)
        @Max(100)
        @DefaultValue("25")
        limit : Int,

        @ApiParam(hidden = true)
        @QueryParam(ListingParams.offset)
        @Min(0)
        @DefaultValue("0")
        offset : Int,

        @ApiParam(hidden = true)
        @QueryParam(ListingParams.sortBy)
        @DefaultValue(UserContract.ID)
        @Pattern(message = "value should be one of User's DTO fields.",
            regexp = UserContract.ENTRY_REGEX)
        sortBy : String,

        @ApiParam(hidden = true)
        @QueryParam(ListingParams.sortOrder)
        @DefaultValue("UNSORTED")
        sortOrder : SortOrder,

        @ApiParam(hidden = true)
        @QueryParam(ListingParams.showActive)
        @DefaultValue("false")
        showActive : Boolean): Response = ResponseUtils.responseFromTryCatch {
        ListingParams(limit = limit, offset = offset, sortBy = sortBy, sortOrder = sortOrder, showActive = showActive)
            .let(service::getAllUsers)
            .let(ResponseUtils::mapResult)
    }

    @Path("{id}")
    @GET
    @ApiOperation(
        value = "Returns the user which has same id.",
        response = User::class,
        code = HttpStatus.OK_200,
        notes = "Returns list of existent users"
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    fun getUserById(@ApiParam(hidden = true) @PathParam("id") id: Int): Response = ResponseUtils.responseFromTryCatch {
        service.getUserById(id)
            .let(ResponseUtils::mapResult)
    }

    @Path("{id}")
    @DELETE
    @ApiOperation(
        value = "Deletes the user which has same id.",
        response = User::class,
        code = HttpStatus.OK_200,
        notes = "Returns list of existent users"
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    fun deleteUserById(@ApiParam(hidden = true) @PathParam("id") id: Int): Response = ResponseUtils.responseFromTryCatch {
        service.deleteUserById(id)
            .let(ResponseUtils::mapResult)
    }

    @POST
    @ApiOperation(
        value = "Creates and saves new user.",
        response = User::class,
        code = HttpStatus.OK_200,
        notes = "Returns list of existent users"
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun addNewUser(newUser: NewUser): Response = ResponseUtils.responseFromTryCatch {
        service.addUser(newUser)
            .let(ResponseUtils::mapResult)
    }

    @PATCH
    @ApiOperation(
        value = "Updates the user's which has same id.",
        response = User::class,
        code = HttpStatus.OK_200,
        notes = "Returns list of existent users"
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun updateUser(updatedUser: UpdateUser): Response = ResponseUtils.responseFromTryCatch {
        service.updateUser(updatedUser)
            .let(ResponseUtils::mapResult)
    }

    companion object {
        const val PATH = "/users"
    }
}

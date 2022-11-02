package controller

import com.codahale.metrics.annotation.Timed
import entities.ListingParams
import service.UserManagementService
import model.user.*
import utils.*
import java.util.*
import javax.swing.SortOrder
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
class UsersResourceController(private val service: UserManagementService) {

    @GET
    @Timed
    fun getAllUsers(@QueryParam(ListingParams.filterBy) filterBy: Optional<String>,
                    @QueryParam(ListingParams.limit) @Min(1) @Max(100) @DefaultValue("25") limit : Int,
                    @QueryParam(ListingParams.offset) @Min(0) @DefaultValue("0") offset : Int,
                    @QueryParam(ListingParams.sortBy) @DefaultValue(UserContract.ID) sortBy : String,
                    @QueryParam(ListingParams.sortOrder) @DefaultValue("UNSORTED") sortOrder : SortOrder,
                    @QueryParam(ListingParams.showActive) @DefaultValue("false") showActive : Boolean): Response =
        ListingParams(filterBy=OptionalUtils.getOrNull(filterBy)?.let(RequestProcessingUtil::parseFilterPair),
            limit = limit, offset = offset, sortBy = sortBy,
            sortOrder = sortOrder, showActive = showActive
        ).let(service::getAllUsers)
            .let(ResponseUtils::mapResult)

    @Path("{id}")
    @GET
    @Timed
    fun getUserById(@PathParam("id") id: Int): Response =
        service.getUserById(id)
            .let(ResponseUtils::mapResult)

    @Path("{id}")
    @DELETE
    @Timed
    fun deleteUserById(@PathParam("id") id: Int): Response =
        service.deleteUserById(id)
            .let(ResponseUtils::mapResult)

    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun addNewUser(newUser: Optional<NewUser>): Response =
        RequestProcessingUtil.checkRequiredBodyEntity(Pair("User", newUser))
            .let(service::addUser)
            .let(ResponseUtils::mapResult)

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun updateUser(updatedUser: Optional<UpdateUser>): Response =
        RequestProcessingUtil.checkRequiredBodyEntity(Pair("User", updatedUser))
            .let(service::updateUser)
            .let(ResponseUtils::mapResult)
}

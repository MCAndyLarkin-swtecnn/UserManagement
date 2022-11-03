package controller

import com.codahale.metrics.annotation.Timed
import entities.ListingParams
import service.IStubService
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
//TODO: Demo IStubService. Should be deleted! UserManagementService should be implemented and used instead.
class UsersResourceController(private val service: IStubService) {

    //valid: http://localhost:8080/appName/users?filterBy=id-3&limit=10&offset=3&sortBy=email&sortOrder=ASCENDING&showActive=true
    @GET
    @Timed
    fun getAllUsers(@QueryParam(ListingParams.filterBy) filterBy: Optional<String>,
                    @QueryParam(ListingParams.limit) @Min(1) @Max(100) @DefaultValue("25") limit : OptionalInt,
                    @QueryParam(ListingParams.offset) @Min(0) @DefaultValue("0") offset : OptionalInt,
                    @QueryParam(ListingParams.sortBy) @DefaultValue(UserContract.ID) sortBy : Optional<String>,
                    @QueryParam(ListingParams.sortOrder) @DefaultValue("UNSORTED") sortOrder : Optional<SortOrder>,
                    @QueryParam(ListingParams.showActive) @DefaultValue("false") showActive : Optional<Boolean>): Response = ResponseUtils.responseFromTryCatch {
        ListingParams(
            filterBy = OptionalUtils.getOrNull(filterBy)?.let(RequestProcessingUtil::parseFilterPair),
            limit = OptionalUtils.getOrNull(limit), offset = OptionalUtils.getOrNull(offset),
            sortBy = OptionalUtils.getOrNull(sortBy), sortOrder = OptionalUtils.getOrNull(sortOrder),
            showActive = OptionalUtils.getOrNull(showActive) ?: false
        ).let(service::getAllUsers)
            .let(ResponseUtils::mapResult)
    }

    @Path("{id}")
    @GET
    @Timed
    fun getUserById(@PathParam("id") id: Int): Response = ResponseUtils.responseFromTryCatch {
        service.getUserById(id)
            .let(ResponseUtils::mapResult)
    }

    @Path("{id}")
    @DELETE
    @Timed
    fun deleteUserById(@PathParam("id") id: Int): Response = ResponseUtils.responseFromTryCatch {
        service.deleteUserById(id)
            .let(ResponseUtils::mapResult)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun addNewUser(newUser: Optional<NewUser>): Response = ResponseUtils.responseFromTryCatch {
        RequestProcessingUtil.checkRequiredBodyEntity(Pair("User", newUser))
            .let(service::addUser)
            .let(ResponseUtils::mapResult)
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Timed
    fun updateUser(updatedUser: Optional<UpdateUser>): Response = ResponseUtils.responseFromTryCatch {
        RequestProcessingUtil.checkRequiredBodyEntity(Pair("User", updatedUser))
            .let(service::updateUser)
            .let(ResponseUtils::mapResult)
    }
}
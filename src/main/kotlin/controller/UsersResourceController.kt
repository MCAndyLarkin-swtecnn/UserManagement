package controller

import service.UserManagementService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/userManagement")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
class UsersResourceController(private val service: UserManagementService) {
}
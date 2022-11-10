package controller

import entities.ListingParams
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import gateway.controller.UsersResourceController
import io.mockk.*
import gateway.model.user.User
import gateway.model.user.UserContract
import io.dropwizard.testing.junit5.ResourceExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import service.UserManagementService
import javax.swing.SortOrder
import javax.ws.rs.BadRequestException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.client.Entity
import kotlin.Exception
import kotlin.test.assertEquals
import kotlin.test.assertIs
import model.user.TestUserModelsUtils
import model.user.TestUserModelsUtils.toNewUser
import model.user.TestUserModelsUtils.toUpdateUser


typealias MockEvery<T> = MockKMatcherScope.() -> T

@ExtendWith(DropwizardExtensionsSupport::class)
internal class UsersResourceControllerTest {
    private val service: UserManagementService = mockk()
    private val controllerExtension: ResourceExtension = ResourceExtension.builder()
        .addResource(UsersResourceController(service))
        .build()

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }


    //::getAllUsers

    @Test
    fun getAllUsers_withDefaultParams() {
        val defaultParams = ListingParams(
            25,
            0,
            UserContract.ID,
            SortOrder.UNSORTED,
            false
        )
        val expectedResult = listOf<User>()
        every { service.getAllUsers(any()) } returns Result.success(expectedResult)

        val actualResult = controllerExtension.target(UsersResourceController.PATH).request().get(List::class.java)

        verify { service.getAllUsers(defaultParams) }
        assertEquals(expectedResult, actualResult, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun getAllUsers_withValidParams() {
        val expectedParams = ListingParams(
            4,
            2,
            UserContract.EMAIL,
            SortOrder.ASCENDING,
            true
        )
        val expectedResult = listOf<User>()
        every { service.getAllUsers(any()) } returns Result.success(expectedResult)

        val actualResult = controllerExtension.target(UsersResourceController.PATH)
            .queryParam(ListingParams.limit, expectedParams.limit)
            .queryParam(ListingParams.offset, expectedParams.offset)
            .queryParam(ListingParams.sortBy, expectedParams.sortBy)
            .queryParam(ListingParams.sortOrder, expectedParams.sortOrder)
            .queryParam(ListingParams.showActive, expectedParams.showActive)
            .request().get(List::class.java)

        verify { service.getAllUsers(expectedParams) }
        assertEquals(expectedResult, actualResult, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun getAllUsers_withInvalidParams() {
        every { service.getAllUsers(any()) } returns Result.success(mockk())

        assertThrows<BadRequestException> {
            controllerExtension.target(UsersResourceController.PATH)
                .queryParam(ListingParams.limit, 100)
                .queryParam(ListingParams.offset, -1)
                .queryParam(ListingParams.sortBy, "")
                .queryParam(ListingParams.sortOrder, "")
                .queryParam(ListingParams.showActive, "")
                .request().get(List::class.java)
        }
        verify(inverse = true) { service.getAllUsers(any()) }
    }

    @Test
    fun getAllUsers_serviceThrowsException() {
        testServiceThrowsException({ service.getAllUsers(any()) }) {
            controllerExtension.target(UsersResourceController.PATH).request().get(List::class.java)
        }
    }

    @Test
    fun getAllUsers_serviceReturnsException() {
        testServiceReturnException({ service.getAllUsers(any()) }) {
            controllerExtension.target(UsersResourceController.PATH).request().get(List::class.java)
        }
    }


    //::getUserById

    @Test
    fun getUserById_regular() {
        val expectedIdParam = 2
        val expectedUser = TestUserModelsUtils.getTestUser()

        every{ service.getUserById(any()) } returns Result.success(expectedUser)

        val actualResult = controllerExtension.target(UsersResourceController.PATH)
            .path(expectedIdParam.toString())
            .request()
            .get(User::class.java)

        verify{ service.getUserById(expectedIdParam) }
        assertEquals(actualResult, expectedUser, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun getUserById_serviceThrowsException() {
        testServiceThrowsException({ service.getUserById(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .path("2")
                .request()
                .get(User::class.java)
        }
    }

    @Test
    fun getUserById_serviceReturnsException() {
        testServiceReturnException({ service.getUserById(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .path("2")
                .request()
                .get(User::class.java)
        }
    }


    //::deleteUserById

    @Test
    fun deleteUserById_regular() {
        val expectedIdParam = 2
        val expectedUser = TestUserModelsUtils.getTestUser()

        every{ service.deleteUserById(any()) } returns Result.success(expectedUser)

        val actualResult = controllerExtension.target(UsersResourceController.PATH)
            .path(expectedIdParam.toString())
            .request()
            .delete(User::class.java)

        verify{ service.deleteUserById(expectedIdParam) }
        assertEquals(actualResult, expectedUser, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun deleteUserById_serviceThrowsException() {
        testServiceThrowsException({ service.deleteUserById(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .path("2")
                .request()
                .delete(User::class.java)
        }
    }

    @Test
    fun deleteUserById_serviceReturnsException() {
        testServiceReturnException({ service.deleteUserById(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .path("2")
                .request()
                .delete(User::class.java)
        }
    }


    //::addNewUser

    @Test
    fun addNewUser_regular() {
        val expectedUser = TestUserModelsUtils.getTestUser()
        val newUser = expectedUser.toNewUser()

        every{ service.addUser(any()) } returns Result.success(expectedUser)

        val actualResult = controllerExtension.target(UsersResourceController.PATH)
            .request()
            .post(Entity.json(newUser), User::class.java)

        verify{ service.addUser(newUser) }
        assertEquals(actualResult, expectedUser, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun addNewUser_invalidParam() {
        every { service.addUser(any()) } returns Result.success(TestUserModelsUtils.getTestUser())

        assertThrows<Exception> {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .post(Entity.json(""), User::class.java)
        }

        assertThrows<Exception> {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .post(Entity.text(""), User::class.java)
        }
        verify(inverse = true) { service.addUser(any()) }
    }

    @Test
    fun addNewUser_serviceThrowsException() {
        testServiceThrowsException({ service.addUser(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .post(Entity.json(TestUserModelsUtils.getTestNewUser()), User::class.java)
        }
    }

    @Test
    fun addNewUser_serviceReturnsException() {
        testServiceReturnException({ service.addUser(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .post(Entity.json(TestUserModelsUtils.getTestNewUser()), User::class.java)
        }
    }


    //::updateUser

    @Test
    fun updateUser_regular() {
        val expectedUser = TestUserModelsUtils.getTestUser()
        val updatedUser = expectedUser.toUpdateUser()

        every{ service.updateUser(any()) } returns Result.success(expectedUser)

        val actualResult = controllerExtension.target(UsersResourceController.PATH)
            .request()
            .method("PATCH", Entity.json(updatedUser), User::class.java)

        verify{ service.updateUser(updatedUser) }
        assertEquals(actualResult, expectedUser, "Result returned by service is not equals to expected one.")
    }

    @Test
    fun updateUser_invalidParam() {
        every { service.updateUser(any()) } returns Result.success(TestUserModelsUtils.getTestUser())

        assertThrows<Exception> {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .method("PATCH", Entity.json(""), User::class.java)
        }

        assertThrows<Exception> {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .method("PATCH", Entity.text(""), User::class.java)
        }
        verify(inverse = true) { service.updateUser(any()) }
    }

    @Test
    fun updateUser_serviceThrowsException() {
        testServiceThrowsException({ service.updateUser(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .method("PATCH", Entity.json(TestUserModelsUtils.getTestUpdatedUser()), User::class.java)
        }
    }

    @Test
    fun updateUser_serviceReturnsException() {
        testServiceReturnException({ service.updateUser(any()) }) {
            controllerExtension.target(UsersResourceController.PATH)
                .request()
                .method("PATCH", Entity.json(TestUserModelsUtils.getTestUpdatedUser()), User::class.java)
        }
    }

    companion object {
        private fun <T, R> testServiceThrowsException(serviceEvery: MockEvery<Result<T>>,
                                                      controllerTesting: () -> R) {
            val expectedExceptionMessage = "ServiceThrowedException"
            every(serviceEvery) throws Exception(expectedExceptionMessage)

            try {
                controllerTesting()
                assert(false) {"An exception was expected. But it was not."}
            } catch (ex: Throwable) {
                assertIs<WebApplicationException>(ex)
                assertEquals(ex.response.statusInfo.reasonPhrase, expectedExceptionMessage)
            }
        }

        private fun <T, R> testServiceReturnException(serviceEvery: MockEvery<Result<T>>,
                                                      controllerTesting: () -> R) {
            val expectedExceptionMessage = "ServiceReturnedException"
            every(serviceEvery) returns Result.failure(Exception(expectedExceptionMessage))

            try {
                controllerTesting()
                assert(false) {"An exception was expected. But it was not."}
            } catch (ex: Throwable) {
                assertIs<WebApplicationException>(ex)
                assertEquals(ex.response.statusInfo.reasonPhrase, expectedExceptionMessage)
            }
        }
    }
}
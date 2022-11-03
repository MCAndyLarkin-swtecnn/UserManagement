package controller

import entities.ListingParams
import io.mockk.*
import model.user.NewUser
import model.user.UpdateUser
import model.user.User
import model.user.UserContract
import org.junit.jupiter.api.*
import service.UserManagementService
import utils.RequestProcessingUtil
import java.lang.Exception
import java.util.*
import javax.swing.SortOrder
import javax.ws.rs.core.Response
import kotlin.test.assertEquals


typealias MockVerify = MockKVerificationScope.() -> Unit
typealias MockEvery<T> = MockKMatcherScope.() -> T

internal class UsersResourceControllerTest {
    private lateinit var controller: UsersResourceController
    private lateinit var service: UserManagementService

    @BeforeEach
    fun setUp() {
        service = mockk()
        controller = UsersResourceController(service)
    }


    //::getAllUsers

    @Test
    fun getAllUsers_withValidParams() {
        val expectedParams = ListingParams(
            Pair("a", "b"),
            4,
            2,
            UserContract.EMAIL,
            SortOrder.ASCENDING,
            true
        )
        val expectedResult = listOf<User>()
        every { service.getAllUsers(any()) } returns Result.success(expectedResult)

        val actualResult = controller.getAllUsers(
            Optional.of("a${RequestProcessingUtil.FILTER_DELIMITER}b"),
            OptionalInt.of(4),
            OptionalInt.of(2),
            Optional.of(UserContract.EMAIL),
            Optional.of(SortOrder.ASCENDING),
            Optional.of(true)
        )

        verify { service.getAllUsers(expectedParams) }
        assertEquals(actualResult.status, Response.Status.OK.statusCode)
        assert(actualResult.entity is List<*>) { "Invalid response entity. Expected: List" }
        assertEquals(actualResult.entity, expectedResult, "Result returned by service is npt equals to expected one.")
    }

    @Test
    fun getAllUsers_withInvalidParams() {
        val actualResult = controller.getAllUsers(
            Optional.of("ab"),
            OptionalInt.of(1000),
            OptionalInt.of(1000),
            Optional.of(""),
            Optional.of(SortOrder.ASCENDING),
            Optional.ofNullable(null)
        )

        verify(inverse = true) { service.getAllUsers(any()) }
        assertEquals(actualResult.status, Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun getAllUsers_withoutParams() {
        val expectedParams = ListingParams(null, null, null, null, null, false)
        val expectedResult = listOf<User>()
        every { service.getAllUsers(any()) } returns Result.success(expectedResult)

        val actualResult = controller.getAllUsers(Optional.empty(), OptionalInt.empty(), OptionalInt.empty(),
            Optional.empty(), Optional.empty(), Optional.empty())

        verify { service.getAllUsers(expectedParams) }
        assertEquals(actualResult.status, Response.Status.OK.statusCode)
        assert(actualResult.entity is List<*>) { "Invalid response entity. Expected: List" }
        assertEquals(actualResult.entity, expectedResult, "Result returned by service is npt equals to expected one.")
    }

    @Test
    fun getAllUsers_serviceThrowsException() {
        testServiceThrowsException({ service.getAllUsers(any()) }) {
            controller.getAllUsers(Optional.empty(), OptionalInt.empty(), OptionalInt.empty(),
                Optional.empty(), Optional.empty(), Optional.empty())
        }
    }

    @Test
    fun getAllUsers_serviceReturnsException() {
        testServiceReturnException({ service.getAllUsers(any()) }) {
            controller.getAllUsers(Optional.empty(), OptionalInt.empty(), OptionalInt.empty(),
                Optional.empty(), Optional.empty(), Optional.empty())
        }
    }


    //::getUserById

    @Test
    fun getUserById_regular() {
        val expectedIdParam = 2
        testRegularServiceUsing({ service.getUserById(any()) },
            { service.getUserById(expectedIdParam) },
            { controller.getUserById(expectedIdParam) }
        )
    }

    @Test
    fun getUserById_serviceThrowsException() {
        testServiceThrowsException({ service.getUserById(any()) }) {
            controller.getUserById(2)
        }
    }

    @Test
    fun getUserById_serviceReturnsException() {
        testServiceReturnException({ service.getUserById(any()) }) {
            controller.getUserById(2)
        }
    }


    //::deleteUserById

    @Test
    fun deleteUserById_regular() {
        val expectedIdParam = 2
        testRegularServiceUsing({ service.deleteUserById(any()) },
            { service.deleteUserById(expectedIdParam) },
            { controller.deleteUserById(expectedIdParam) }
        )
    }

    @Test
    fun deleteUserById_serviceThrowsException() {
        testServiceThrowsException({ service.deleteUserById(any()) }) {
            controller.deleteUserById(2)
        }
    }

    @Test
    fun deleteUserById_serviceReturnsException() {
        testServiceReturnException({ service.deleteUserById(any()) }) {
            controller.deleteUserById(2)
        }
    }


    //::addNewUser

    @Test
    fun addNewUser_regular() {
        val user = mockk<NewUser>()
        testRegularServiceUsing({ service.addUser(any()) },
            { service.addUser(user) },
            { controller.addNewUser(Optional.of(user)) }
        )
    }

    @Test
    fun addNewUser_missedRequiredParam() {
        val actualResult = controller.addNewUser(Optional.ofNullable(null))

        verify(inverse = true) { service.addUser(any()) }
        assertEquals(actualResult.status, Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun addNewUser_serviceThrowsException() {
        testServiceThrowsException({ service.addUser(any()) }) {
            controller.addNewUser(Optional.of(mockk()))
        }
    }

    @Test
    fun addNewUser_serviceReturnsException() {
        testServiceReturnException({ service.addUser(any()) }) {
            controller.addNewUser(Optional.of(mockk()))
        }
    }


    //::updateUser

    @Test
    fun updateUser_regular() {
        val user = mockk<UpdateUser>()
        testRegularServiceUsing({ service.updateUser(any()) },
            { service.updateUser(user) },
            { controller.updateUser(Optional.of(user)) }
        )
    }

    @Test
    fun updateUser_missedRequiredParam() {
        val actualResult = controller.updateUser(Optional.ofNullable(null))

        verify(inverse = true) { service.updateUser(any()) }
        assertEquals(actualResult.status, Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun updateUser_serviceThrowsException() {
        val user = mockk<UpdateUser>()
        testServiceThrowsException({ service.updateUser(any()) }) {
            controller.updateUser(Optional.of(user))
        }
    }

    @Test
    fun updateUser_serviceReturnsException() {
        val user = mockk<UpdateUser>()
        testServiceReturnException({ service.updateUser(any()) }) {
            controller.updateUser(Optional.of(user))
        }
    }

    private fun <T> testServiceThrowsException(serviceEvery: MockEvery<Result<T>>,
                                               controllerTesting: () -> Response) {
        val expectedReason = "Expected"
        val expectedException = Exception(expectedReason)
        every(serviceEvery) throws expectedException

        val actualResult = controllerTesting()

        assertEquals(actualResult.status, Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        assertEquals(actualResult.statusInfo.reasonPhrase, expectedReason)
    }

    private fun <T> testServiceReturnException(serviceEvery: MockEvery<Result<T>>,
                                               controllerTesting: () -> Response) {
        val expectedReason = "Expected"
        val expectedException = Exception(expectedReason)
        every(serviceEvery) returns Result.failure(expectedException)

        val actualResult = controllerTesting()
        assertEquals(actualResult.status, Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        assertEquals(actualResult.statusInfo.reasonPhrase, expectedReason)
    }

    private inline fun <reified T: Any> testRegularServiceUsing(noinline serviceEvery: MockEvery<Result<T>>,
                                                                noinline serviceVerify: MockVerify,
                                                                controllerTesting: () -> Response) {
        val expectedResult = mockk<T>()
        every(serviceEvery) returns Result.success(expectedResult)

        val actualResult = controllerTesting()

        verify(verifyBlock = serviceVerify)
        assertEquals(actualResult.status, Response.Status.OK.statusCode)
        assert(actualResult.entity is User) { "Invalid response entity. Expected: User" }
        assertEquals(actualResult.entity, expectedResult, "Result returned by service is npt equals to expected one.")
    }
}
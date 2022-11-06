package datasource.dao

import datasource.dao.dbmanager.DatabaseManager
import datasource.dao.model.user.UserDBModel
import datasource.dao.validation.DaoValidator
import entities.ListingParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class UsersDAOImplTest {
    private lateinit var dao: UsersDao
    private lateinit var validator: DaoValidator
    private lateinit var dbManager: DatabaseManager

    @BeforeEach
    fun setUp() {
        validator = mockk()
        dbManager = mockk()
        dao = UsersDAOImpl(dbManager, validator)
    }

    @Test
    fun getAllUsers() {
        val expectedParam = mockk<ListingParams>()
        val expectedResult = mockk<List<UserDBModel>>()
        every { dbManager.getAllUsers(any()) } returns expectedResult

        val actualResult = dao.getAllUsers(expectedParam)

        verify { dbManager.getAllUsers(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserById() {
        val expectedParam = 5
        val expectedResult = mockk<UserDBModel>()
        every { dbManager.getUserById(any()) } returns expectedResult

        val actualResult = dao.getUserById(expectedParam)

        verify { dbManager.getUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserByEmail() {
        val expectedParam = "expected email"
        val expectedResult = mockk<UserDBModel>()
        every { dbManager.getUserByEmail(any()) } returns expectedResult

        val actualResult = dao.getUserByEmail(expectedParam)

        verify { dbManager.getUserByEmail(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deleteUserById() {
        val expectedParam = 5
        val expectedResult = mockk<UserDBModel>()
        every { dbManager.deleteUserById(any()) } returns expectedResult

        val actualResult = dao.deleteUserById(expectedParam)

        verify { dbManager.deleteUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun addUser_userValid() {
        val expectedUserParam = mockk<UserDBModel>()
        every { validator.checkUserValidity(any()) } returns Unit
        every { dbManager.addUser(any()) } returns expectedUserParam

        val actualResult = dao.addUser(expectedUserParam)

        verify { validator.checkUserValidity(expectedUserParam) }
        verify { dbManager.addUser(expectedUserParam) }
        assertEquals(expectedUserParam, actualResult)
    }

    @Test
    fun addUser_userInvalid() {
        val expectedExceptionMessage = "expectedExceptionMessage"
        val expectedException = Exception(expectedExceptionMessage)
        val expectedUserParam = mockk<UserDBModel>()
        every { validator.checkUserValidity(any()) } throws expectedException
        every { dbManager.addUser(any()) } returns mockk()

        assertThrows<Exception> (expectedExceptionMessage) {
            dao.addUser(expectedUserParam)
        }
        verify { validator.checkUserValidity(expectedUserParam) }
        verify(inverse = true) { dbManager.addUser(any()) }
    }

    @Test
    fun updateUser_userValid() {
        val expectedUserParam = mockk<UserDBModel>()
        every { validator.checkUserValidity(any()) } returns Unit
        every { dbManager.updateUser(any()) } returns expectedUserParam

        val actualResult = dao.updateUser(expectedUserParam)

        verify { validator.checkUserValidity(expectedUserParam) }
        verify { dbManager.updateUser(expectedUserParam) }
        assertEquals(expectedUserParam, actualResult)
    }

    @Test
    fun updateUser_userInvalid() {
        val expectedExceptionMessage = "expectedExceptionMessage"
        val expectedException = Exception(expectedExceptionMessage)
        val expectedUserParam = mockk<UserDBModel>()
        every { validator.checkUserValidity(any()) } throws expectedException
        every { dbManager.updateUser(any()) } returns mockk()

        assertThrows<Exception> (expectedExceptionMessage) {
            dao.updateUser(expectedUserParam)
        }
        verify { validator.checkUserValidity(expectedUserParam) }
        verify(inverse = true) { dbManager.updateUser(any()) }
    }
}
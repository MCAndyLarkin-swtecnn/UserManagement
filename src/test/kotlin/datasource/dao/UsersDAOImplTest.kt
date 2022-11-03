package datasource.dao

import datasource.dao.validation.DaoValidator
import entities.ListingParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.user.User
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class UsersDAOImplTest {
    private lateinit var dao: UsersDao
    private lateinit var validator: DaoValidator
    private lateinit var jdbi: Jdbi
    private lateinit var mappedDao: UsersMappedDao

    @BeforeEach
    fun setUp() {
        jdbi = mockk()
        mappedDao = mockk()
        every { jdbi.onDemand<UsersMappedDao>(any()) } returns mappedDao
        validator = mockk()
        dao = UsersDAOImpl(jdbi, validator)
    }

    @Test
    fun getAllUsers() {
        val expectedParam = mockk<ListingParams>()
        val expectedResult = mockk<List<User>>()
        every { mappedDao.getAllUsers(any()) } returns expectedResult

        val actualResult = dao.getAllUsers(expectedParam)

        verify { mappedDao.getAllUsers(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserById() {
        val expectedParam = 5
        val expectedResult = mockk<User>()
        every { mappedDao.getUserById(any()) } returns expectedResult

        val actualResult = dao.getUserById(expectedParam)

        verify { mappedDao.getUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deleteUserById() {
        val expectedParam = 5
        val expectedResult = mockk<User>()
        every { mappedDao.getUserById(any()) } returns expectedResult
        every { mappedDao.deleteUserById(any()) } returns mockk()

        val actualResult = dao.deleteUserById(expectedParam)

        verify { mappedDao.deleteUserById(expectedParam) }
        verify { mappedDao.getUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun addUser_userValid() {
        val expectedUserParam = mockk<User>()
        val expectedIdParam = 0
        every { validator.checkUserValidity(any()) } returns Unit
        every { mappedDao.insertUser(any()) } returns expectedIdParam
        every { mappedDao.getUserById(any()) } returns expectedUserParam

        val actualResult = dao.addUser(expectedUserParam)

        verify { validator.checkUserValidity(expectedUserParam) }
        verify { mappedDao.insertUser(expectedUserParam) }
        verify { mappedDao.getUserById(expectedIdParam) }
        assertEquals(expectedUserParam, actualResult)
    }

    @Test
    fun addUser_userInvalid() {
        val expectedExceptionMessage = "expectedExceptionMessage"
        val expectedException = Exception(expectedExceptionMessage)
        val expectedUserParam = mockk<User>()
        every { validator.checkUserValidity(any()) } throws expectedException
        every { mappedDao.insertUser(any()) } returns mockk()
        every { mappedDao.getUserById(any()) } returns mockk()

        assertThrows<Exception> (expectedExceptionMessage) {
            dao.addUser(expectedUserParam)
        }
        verify { validator.checkUserValidity(expectedUserParam) }
        verify(inverse = true) { mappedDao.insertUser(any()) }
        verify(inverse = true) { mappedDao.getUserById(any()) }
    }

    @Test
    fun updateUser_userValid() {
        val expectedUserParam = mockk<User>()
        val expectedIdParam = 0
        every { validator.checkUserValidity(any()) } returns Unit
        every { mappedDao.updateUser(any()) } returns expectedIdParam
        every { mappedDao.getUserById(any()) } returns expectedUserParam

        val actualResult = dao.updateUser(expectedUserParam)

        verify { validator.checkUserValidity(expectedUserParam) }
        verify { mappedDao.updateUser(expectedUserParam) }
        verify { mappedDao.getUserById(expectedIdParam) }
        assertEquals(expectedUserParam, actualResult)
    }

    @Test
    fun updateUser_userInvalid() {
        val expectedExceptionMessage = "expectedExceptionMessage"
        val expectedException = Exception(expectedExceptionMessage)
        val expectedUserParam = mockk<User>()
        every { validator.checkUserValidity(any()) } throws expectedException
        every { mappedDao.updateUser(any()) } returns mockk()
        every { mappedDao.getUserById(any()) } returns mockk()

        assertThrows<Exception> (expectedExceptionMessage) {
            dao.updateUser(expectedUserParam)
        }
        verify { validator.checkUserValidity(expectedUserParam) }
        verify(inverse = true) { mappedDao.updateUser(any()) }
        verify(inverse = true) { mappedDao.getUserById(any()) }
    }
}
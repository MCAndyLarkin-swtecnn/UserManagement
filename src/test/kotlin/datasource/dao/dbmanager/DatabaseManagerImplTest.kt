package datasource.dao.dbmanager

import datasource.dao.UsersMappedDao
import datasource.dao.model.user.UserDBModel
import entities.ListingParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DatabaseManagerImplTest {
    private lateinit var dbManager: DatabaseManager
    private lateinit var jdbi: Jdbi
    private lateinit var mappedDao: UsersMappedDao

    @BeforeEach
    fun setUp() {
        jdbi = mockk()
        mappedDao = mockk()
        every { jdbi.onDemand<UsersMappedDao>(any()) } returns mappedDao
        dbManager = DatabaseManagerImpl(jdbi)
    }

    @Test
    fun getAllUsers() {
        val expectedParam = mockk<ListingParams>()
        val expectedResult = mockk<List<UserDBModel>>()
        every { mappedDao.getAllUsers(any()) } returns expectedResult

        val actualResult = dbManager.getAllUsers(expectedParam)

        verify { mappedDao.getAllUsers(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserById() {
        val expectedParam = 5
        val expectedResult = mockk<UserDBModel>()
        every { mappedDao.getUserById(any()) } returns expectedResult

        val actualResult = dbManager.getUserById(expectedParam)

        verify { mappedDao.getUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deleteUserById() {
        val expectedParam = 5
        val expectedResult = mockk<UserDBModel>()
        every { mappedDao.getUserById(any()) } returns expectedResult
        every { mappedDao.deleteUserById(any()) } returns mockk()

        val actualResult = dbManager.deleteUserById(expectedParam)

        verify { mappedDao.deleteUserById(expectedParam) }
        verify { mappedDao.getUserById(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserByEmail() {
        val expectedParam = "expected email"
        val expectedResult = mockk<UserDBModel>()
        every { mappedDao.getUserByEmail(any()) } returns expectedResult

        val actualResult = dbManager.getUserByEmail(expectedParam)

        verify { mappedDao.getUserByEmail(expectedParam) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun addUser() {
        val expectedUserParam = mockk<UserDBModel>()
        val expectedIdParam = 0
        every { mappedDao.insertUser(any()) } returns expectedIdParam
        every { mappedDao.getUserById(any()) } returns expectedUserParam

        val actualResult = dbManager.addUser(expectedUserParam)

        verify { mappedDao.insertUser(expectedUserParam) }
        verify { mappedDao.getUserById(expectedIdParam) }
        assertEquals(expectedUserParam, actualResult)
    }

    @Test
    fun updateUser() {
        val expectedUserParam = mockk<UserDBModel>()
        val expectedIdParam = 0
        every { mappedDao.updateUser(any()) } returns expectedIdParam
        every { mappedDao.getUserById(any()) } returns expectedUserParam

        val actualResult = dbManager.updateUser(expectedUserParam)

        verify { mappedDao.updateUser(expectedUserParam) }
        verify { mappedDao.getUserById(expectedIdParam) }
        assertEquals(expectedUserParam, actualResult)
    }
}
package datasource.dao

import datasource.dao.model.user.UserDBModel
import datasource.dao.validation.DaoValidator
import entities.ListingParams
import gateway.model.user.UserContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.junit.jupiter.api.*
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

internal class UsersDAOImplTest {
    private lateinit var dao: UsersDao
    private lateinit var validator: DaoValidator
    private lateinit var jdbi: Jdbi

    class ExpectedDaoException : Exception()

    @BeforeEach
    fun setUp() {
        jdbi = createTestJdbi()
        validator = mockk()
        dao = UsersDAOImpl(jdbi, validator)
    }

    @AfterEach
    fun tearDrop() {
        jdbi.withHandle<Any, Exception> { handle ->
            handle.execute("DROP TABLE ${UsersMappedDao.USERS_TABLE_NAME};")
        }
    }

    @Test
    fun getAllUsers_emptyOnStart() {
        assertEquals(dao.getAllUsers(null), listOf())
    }

    @Test
    fun getAllUsers_onFilled() {
        val expectedUser = addRandomUserToTable(1)

        val actualResult = dao.getAllUsers(null)

        assert(actualResult.size == 1)
        assertEquals(expectedUser, actualResult.first())
    }

    @Test
    fun getAllUsers_onFilled_withParams() {
        val offset = 1
        val limit = 2
        val expectedUsers = (1..4)
            .map { i -> addRandomUserToTable(i) }
            .subList(offset, offset + limit)
            .sortedBy { user -> user.id }

        val actualResult = dao.getAllUsers(
            ListingParams(
                limit = limit,
                offset = offset,
                sortBy = UserContract.ID,
                sortOrder = null,
                showActive = false
            )
        )

        assertEquals(expectedUsers.size, actualResult.size)
        assertEquals(expectedUsers, actualResult)
    }

    @Test
    fun getUserById_onAbsent() {
        val expectedUserId = 1
        addRandomUserToTable(expectedUserId)

        val actualResult = dao.getUserById(2)

        assertNull(actualResult)
    }

    @Test
    fun getUserById_onExistent() {
        val expectedUserId = 1
        val expectedUser = addRandomUserToTable(expectedUserId)

        val actualResult = dao.getUserById(expectedUserId)

        assertEquals(expectedUser, actualResult)
    }

    @Test
    fun getUserByEmail_onAbsent() {
        val actualResult = dao.getUserById(1)

        assertNull(actualResult)
    }

    @Test
    fun getUserByEmail_onExistent() {
        val expectedUserId = 1
        val expectedUser = addRandomUserToTable(expectedUserId)

        val actualResult = dao.getUserById(expectedUserId)

        assertEquals(actualResult?.email, expectedUser.email)
    }

    @Test
    fun addUser_onValidUser() {
        assert(getAllUsers().isEmpty())
        val expectedUser = getRandomUser(1)
        every { validator.checkUserValidity(any()) } returns Unit

        dao.addUser(expectedUser)

        verify { validator.checkUserValidity(expectedUser)  }
        val result = getAllUsers()
        assert(result.size == 1) {"Result user's list size is more than 1 (${result.size})!"}
        assertEquals(expectedUser, result.first())
    }

    @Test
    fun addUser_onInvalidUser() {
        assert(getAllUsers().isEmpty())
        val expectedUser = getRandomUser(1)
        every { validator.checkUserValidity(any()) } throws ExpectedDaoException()

        assertThrows<ExpectedDaoException> {
            dao.addUser(expectedUser)
        }

        verify { validator.checkUserValidity(expectedUser) }
        assert(getAllUsers().isEmpty())
    }

    @Test
    fun updateUser_onValidUser() {
        val expectedUserId = 1
        val sourcedUser = addRandomUserToTable(expectedUserId)
        assert(getAllUsers().size == 1)

        every { validator.checkUserValidity(any()) } returns Unit
        val expectedUser = getRandomUser(expectedUserId)
        assertNotEquals(expectedUser, sourcedUser)

        dao.updateUser(expectedUser)

        verify { validator.checkUserValidity(expectedUser)  }
        val result = getAllUsers()
        assert(result.size == 1) {"Result user's list size is more than 1 (${result.size})!"}
        assertEquals(expectedUser, result.first())
    }

    @Test
    fun updateUser_onInvalidUser() {
        assert(getAllUsers().isEmpty())
        val expectedUser = getRandomUser(1)
        every { validator.checkUserValidity(any()) } throws ExpectedDaoException()

        assertThrows<ExpectedDaoException> {
            dao.updateUser(expectedUser)
        }

        verify { validator.checkUserValidity(expectedUser) }
        assert(getAllUsers().isEmpty())
    }

    private fun addRandomUserToTable(id: Int): UserDBModel = getRandomUser(id).also { user ->
        jdbi.withHandle<Any, Exception> { handle ->
            handle.execute(
                "INSERT INTO ${UsersMappedDao.USERS_TABLE_NAME} (${UsersMappedDao.USER_FIRST_NAME_COLUMN}, " +
                        "${UsersMappedDao.USER_SECOND_NAME_COLUMN}, ${UsersMappedDao.USER_EMAIL_COLUMN}, " +
                        "${UsersMappedDao.USER_BIRTHDAY_DATE_COLUMN}, ${UsersMappedDao.USER_CREATION_DATE_COLUMN}) " +
                        "VALUES ('${user.firstName}', '${user.secondName}', '${user.email}', " +
                        "'${h2DefaultDateFormat.format(user.birthdayDate)}', '${h2DefaultDateFormat.format(user.creationDate)}')"
            )
        }
    }

    private fun getAllUsers() = jdbi.withHandle<List<UserDBModel>, Exception> { handle ->
        handle.createQuery("SELECT * FROM ${UsersMappedDao.USERS_TABLE_NAME};")
            .map(UsersMappedDao.UserMapper())
            .list()
    }

    companion object {
        val h2DefaultDateFormat = SimpleDateFormat("yyyy-MM-dd")

        private fun getRandomUser(id: Int): UserDBModel {
            val randomDay = Random.nextInt(0, 28)
            val randomYear = Random.nextInt(1900, 2100)
            val randomMonth = Random.nextInt(0, 12)
            val randomAge = Random.nextInt(0, 50)
            return UserDBModel(
                id, "FirstName$randomDay", "SecondName$randomYear", "email$randomMonth",
                Date(randomYear, randomMonth, randomDay), Date(randomYear+randomAge, randomMonth, randomDay), null
            )
        }

        private fun createTestJdbi(): Jdbi {
            Class.forName("org.h2.Driver")
            return DriverManager.getConnection(
                "jdbc:h2:mem:test",
                "test_username",
                "test_password"
            ).let(Jdbi::create).also { jdbi ->
                jdbi.installPlugin(SqlObjectPlugin())
            }
        }
    }
}
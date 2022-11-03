package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import controller.UsersResourceController
import datasource.JdbiProvider
import datasource.dao.UsersDAOImpl
import datasource.dao.UsersDao
import datasource.dao.validation.DaoValidator
import datasource.dao.validation.DaoValidatorImpl
import org.jdbi.v3.core.Jdbi
import service.UserManagementService
import service.UserManagementServiceImpl
import validation.UsersManagementValidator
import validation.UsersManagementValidatorImpl

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<Jdbi>() with singleton { JdbiProvider.jdbi }
        bind<DaoValidator>() with singleton { DaoValidatorImpl() }
        bind<UsersDao>() with singleton { UsersDAOImpl(instance(), instance()) }
        bind<UsersManagementValidator>() with singleton { UsersManagementValidatorImpl() }
        bind<UserManagementService>() with singleton { UserManagementServiceImpl(instance(), instance()) }
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
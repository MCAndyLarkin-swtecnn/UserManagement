package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import gateway.controller.UsersResourceController
import datasource.JdbiProvider
import datasource.dao.UsersDAOImpl
import datasource.dao.UsersDao
import datasource.dao.dbmanager.DatabaseManager
import datasource.dao.dbmanager.DatabaseManagerImpl
import datasource.dao.validation.DaoValidator
import datasource.dao.validation.DaoValidatorImpl
import mapper.GatewayDbUserMapper
import mapper.GatewayDbUserMapperImpl
import org.jdbi.v3.core.Jdbi
import service.UserManagementService
import service.UserManagementServiceImpl
import validation.UsersManagementValidator
import validation.UsersManagementValidatorImpl

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<Jdbi>() with singleton { JdbiProvider.jdbi }
        bind<DaoValidator>() with singleton { DaoValidatorImpl() }
        bind<DatabaseManager>() with singleton { DatabaseManagerImpl(instance()) }
        bind<UsersDao>() with singleton { UsersDAOImpl(instance(), instance()) }
        bind<UsersManagementValidator>() with singleton { UsersManagementValidatorImpl() }
        bind<GatewayDbUserMapper>() with singleton { GatewayDbUserMapperImpl() }
        bind<UserManagementService>() with singleton { UserManagementServiceImpl(instance(), instance(), instance()) }
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
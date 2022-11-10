package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import gateway.controller.UsersResourceController
import datasource.dao.UsersDAOImpl
import datasource.dao.UsersDao
import datasource.dao.validation.DaoValidator
import datasource.dao.validation.DaoValidatorImpl
import mapper.GatewayDbUserMapper
import mapper.GatewayDbUserMapperImpl
import service.UserManagementService
import service.UserManagementServiceImpl

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<DaoValidator>() with singleton { DaoValidatorImpl() }
        bind<UsersDao>() with singleton { UsersDAOImpl(instance(), instance()) }
        bind<GatewayDbUserMapper>() with singleton { GatewayDbUserMapperImpl() }
        bind<UserManagementService>() with singleton { UserManagementServiceImpl(instance(), instance()) }
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
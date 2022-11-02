package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import controller.UsersResourceController
import service.IStubService
import service.StubService

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<IStubService>() with singleton { StubService() }
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
package di

import health.SingleHealthCheck
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import controller.UsersResourceController

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<SingleHealthCheck>() with singleton { SingleHealthCheck() }
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
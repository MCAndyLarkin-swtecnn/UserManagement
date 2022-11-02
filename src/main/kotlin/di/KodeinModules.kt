package di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import controller.UsersResourceController

object KodeinModules {
    val usersManagementModule = Kodein.Module("users management") {
        bind<UsersResourceController>() with singleton { UsersResourceController(instance()) }
    }
}
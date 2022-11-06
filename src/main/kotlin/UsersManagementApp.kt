import di.KodeinModules
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import gateway.controller.UsersResourceController

class UsersManagementApp : Application<UserManagementConfiguration>() {
    private val kodein = Kodein {
        importOnce(KodeinModules.usersManagementModule)
    }

    override fun initialize(bootstrap: Bootstrap<UserManagementConfiguration>?) {
        bootstrap?.addBundle(AssetsBundle("/dist/", "/", "index.html"))
    }

    override fun run(configuration: UserManagementConfiguration, environment: Environment?) {
        environment?.run {
            val usersResourceController: UsersResourceController by kodein.instance()
            //TODO Decide if Real HealthCheck implementation is necessary
            jersey().register(usersResourceController)
        }
    }

    companion object {
        private const val USER_MANAGEMENT_YAML = "src/main/resources/um.yaml"

        @JvmStatic
        fun main(args: Array<String>) {
            UsersManagementApp().run("server", USER_MANAGEMENT_YAML)
        }
    }
}

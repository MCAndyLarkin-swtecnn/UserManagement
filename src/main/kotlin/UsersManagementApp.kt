import di.KodeinModules
import health.SingleHealthCheck
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import controller.UsersResourceController

class UsersManagementApp : Application<UserManagementConfiguration>() {
    private val kodein = Kodein {
        importOnce(KodeinModules.usersManagementModule)
    }

    override fun initialize(bootstrap: Bootstrap<UserManagementConfiguration>?) {
        bootstrap?.addBundle(AssetsBundle("/assets/", "/", "index.html"))
    }

    override fun run(configuration: UserManagementConfiguration, environment: Environment?) {
        environment?.run {
            val usersResourceController: UsersResourceController by kodein.instance()
            val singleHealthCheck: SingleHealthCheck by kodein.instance()
            healthChecks().register("Single", singleHealthCheck)
            jersey().register(usersResourceController)
        }
    }

    companion object {
        const val USER_MANAGEMENT_YAML = "src/main/resources/um.yaml"

        @JvmStatic
        fun main(args: Array<String>) {
            UsersManagementApp().run("server", USER_MANAGEMENT_YAML)
        }
    }
}

import di.KodeinModules
import gateway.UserManagementConfiguration
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import gateway.controller.UsersResourceController
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class UsersManagementApp : Application<UserManagementConfiguration>() {
    private val kodein = Kodein {
        importOnce(KodeinModules.usersManagementModule)
    }

    override fun initialize(bootstrap: Bootstrap<UserManagementConfiguration>?) {
        bootstrap?.addBundle(object : SwaggerBundle<UserManagementConfiguration>() {
            override fun getSwaggerBundleConfiguration(configuration: UserManagementConfiguration?): SwaggerBundleConfiguration? =
                configuration?.swaggerConfig
        })
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

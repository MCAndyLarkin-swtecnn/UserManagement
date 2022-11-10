import di.KodeinModules
import gateway.UserManagementConfiguration
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import gateway.controller.UsersResourceController
import io.dropwizard.jdbi3.JdbiFactory
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import org.jdbi.v3.core.Jdbi
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class UsersManagementApp : Application<UserManagementConfiguration>() {
    private lateinit var jdbi: Jdbi
    private val kodein = Kodein {
        bind<Jdbi>() with singleton { jdbi }
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
            jdbi = JdbiFactory().build(environment, configuration.dataSourceFactory, "database_jdbi")
            val usersResourceController: UsersResourceController by kodein.instance()
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

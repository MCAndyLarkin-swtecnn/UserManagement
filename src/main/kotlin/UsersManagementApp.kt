import io.dropwizard.Application
import io.dropwizard.setup.Environment

class UsersManagementApp : Application<UserManagementConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            UsersManagementApp().run()
        }
    }

    override fun run(configuration: UserManagementConfiguration?, environment: Environment?) {
        TODO("Not yet implemented")
    }
}
package datasource

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import java.sql.DriverManager

object JdbiProvider {
    val jdbi: Jdbi by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
        //TODO: Catch ClassNotFoundException
        Class.forName(DbProperties.DB_DRIVER_CLASS.value())
        DriverManager.getConnection(
            DbProperties.DB_URL.value(),
            DbProperties.DB_USERNAME.value(),
            DbProperties.DB_PASSWORD.value()
        ).let(Jdbi::create).also { jdbi ->
            jdbi.installPlugin(SqlObjectPlugin())
        }
    }
}
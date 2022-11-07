package datasource

import java.io.IOException
import java.util.*

enum class DbProperties(private val key: String) {
    DB_DRIVER_CLASS("db.driver_class"),
    DB_URL("db.url"),
    DB_USERNAME("db.username"),
    DB_PASSWORD("db.password");

    fun value(): String {
        return applicationProperties.getProperty(key, "")
    }

    companion object {
        private const val propsResourceFilename = "db.properties"

        private val applicationProperties: Properties by lazy {
            Properties().apply {
                try {
                    load(DbProperties::class.java.classLoader.getResourceAsStream(propsResourceFilename))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
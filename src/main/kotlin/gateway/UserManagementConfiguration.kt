package gateway

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class UserManagementConfiguration : Configuration() {
    @JsonProperty("database")
    lateinit var dataSourceFactory: DataSourceFactory

    @JsonProperty("swagger")
    lateinit var swaggerConfig: SwaggerBundleConfiguration
}
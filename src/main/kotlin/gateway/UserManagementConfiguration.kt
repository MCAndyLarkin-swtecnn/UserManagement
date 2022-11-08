package gateway

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class UserManagementConfiguration : Configuration() {
    @JsonProperty("swagger")
    lateinit var swaggerConfig: SwaggerBundleConfiguration
}
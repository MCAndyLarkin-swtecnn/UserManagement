package health

import com.codahale.metrics.health.HealthCheck

class SingleHealthCheck : HealthCheck() {
    override fun check(): Result {
        return Result.builder()
            .withDetail("key", "data")
            .withMessage("message")
            .healthy()
            .build()
    }

}

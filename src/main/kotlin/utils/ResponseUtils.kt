package utils

import javax.ws.rs.core.Response

object ResponseUtils {
    fun processResult(result: Result<*>): Response = if (result.isSuccess) {
        Response.ok(result.getOrNull()).build()
    } else {
        Response.status(Response.Status.INTERNAL_SERVER_ERROR.statusCode,
            result.exceptionOrNull()?.message).build()
    }

    fun responseFromTryCatch(tryAction: () -> Response): Response = try {
        tryAction()
    } catch (ex: Exception) {
        Response.status(Response.Status.INTERNAL_SERVER_ERROR.statusCode, ex.message).build()
    }
}
package utils

import javax.ws.rs.core.Response

object ResponseUtils {
    fun mapResult(result: Result<*>): Response = if (result.isSuccess) {
        Response.ok(result.getOrNull()).build()
    } else {
        result.exceptionOrNull().let {ex ->
            println(ex?.message)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR.statusCode, ex?.message).build()
        }
    }

    fun responseFromTryCatch(tryAction: () -> Response): Response = try {
        tryAction()
    } catch (ex: Exception) {
        println(ex.message)
        Response.status(Response.Status.INTERNAL_SERVER_ERROR.statusCode, ex.message).build()
    }
}

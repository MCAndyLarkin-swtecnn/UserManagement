package utils

import datasource.dao.UnexpectedSQLInjectionException
import exceptions.*
import javax.ws.rs.core.Response

object ResponseUtils {
    fun mapResult(result: Result<*>): Response = if (result.isSuccess) {
        Response.ok(result.getOrNull()).build()
    } else {
        result.exceptionOrNull().let {ex ->
            println("Exception caught (from Result): $ex")
            Response.status(ex?.let(this::getStatus)?.statusCode
                ?: Response.Status.BAD_REQUEST.statusCode, ex?.message).build()
        }
    }

    fun responseFromTryCatch(tryAction: () -> Response): Response = try {
        tryAction()
    } catch (ex: Exception) {
        println("Exception caught: $ex")
        Response.status(getStatus(ex).statusCode, ex.message).build()
    }

    private fun getStatus(ex: Throwable): Response.Status = when (ex) {
        is EmailAlreadyUsedException -> Response.Status.CONFLICT
        is UserWithIdNotFoundException -> Response.Status.NOT_FOUND
        is UnexpectedSQLInjectionException ->
            Response.Status.BAD_REQUEST
        else -> Response.Status.INTERNAL_SERVER_ERROR
    }
}

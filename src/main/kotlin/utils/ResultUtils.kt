package utils

object ResultUtils {
    fun <T> resultFromTryCatch(tryAction: () -> T): Result<T> = try {
        Result.success(tryAction())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
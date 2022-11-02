package utils

object ResultUtils {
    fun <T> createResultFrom(producer: () -> T): Result<T> = try {
        Result.success(producer.invoke())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
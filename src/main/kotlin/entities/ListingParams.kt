package entities

data class ListingParams(
    val filterBy: String? = null,
    val limit: Int? = null,
    val offset: Int? = null,
    val sortBy: String? = null,
    val sortOrder: Boolean = true,
    val showActive: Boolean = false
)

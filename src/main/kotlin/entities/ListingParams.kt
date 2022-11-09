package entities

import javax.swing.SortOrder

data class ListingParams(
    val limit: Int?,
    val offset: Int?,
    val sortBy: String?,
    val sortOrder: SortOrder?,
    val showActive: Boolean = false
) {
    companion object {
        const val filterBy = "filterBy"
        const val limit = "limit"
        const val offset = "offset"
        const val sortBy = "sortBy"
        const val sortOrder = "sortOrder"
        const val showActive = "showActive"
    }
}

package mapper

import datasource.dao.model.user.UserDBModel
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import java.util.*

interface GatewayDbUserMapper {
    fun mapNewUserToDBModel(user: NewUser, id: Int?): UserDBModel
    fun mapUpdateUserToDBModel(user: UpdateUser, creationDate: Date, deletionDate: Date?): UserDBModel

    fun mapDBModelToUser(user: UserDBModel): User
}
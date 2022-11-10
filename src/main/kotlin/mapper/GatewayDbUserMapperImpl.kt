package mapper

import datasource.dao.model.user.UserDBModel
import gateway.model.user.NewUser
import gateway.model.user.UpdateUser
import gateway.model.user.User
import java.util.*

class GatewayDbUserMapperImpl : GatewayDbUserMapper {
    override fun mapNewUserToDBModel(user: NewUser, id: Int?): UserDBModel = with(user) {
        UserDBModel(id ?: UserDBModel.UNKNOWN_ID, firstName, secondName, email, Date(birthdayDate), Date(), null)
    }

    override fun mapUpdateUserToDBModel(user: UpdateUser, creationDate: Date, deletionDate: Date?): UserDBModel = with(user) {
        UserDBModel(id, firstName, secondName, email, Date(birthdayDate), creationDate, deletionDate)
    }

    override fun mapDBModelToUser(user: UserDBModel): User = with(user) {
        User(id, firstName, secondName, email, birthdayDate.time, creationDate.time, deletionDate?.time)
    }
}
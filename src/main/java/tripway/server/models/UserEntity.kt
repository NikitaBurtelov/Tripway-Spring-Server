package tripway.server.models

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "user_entity", schema = "public")
data class UserEntity(
        @Id @Column(name = "id") val id: String = "",
        @Column(name = "email") val email: String = "",
        @Column(name = "nickname") val nickname: String = "",
        //todo убрать хранение пароля
        @Column(name = "password") var password: String = ""
):Serializable

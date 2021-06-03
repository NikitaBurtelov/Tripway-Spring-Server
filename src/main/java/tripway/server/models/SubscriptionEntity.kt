package tripway.server.models

import javax.persistence.*

@Entity
@Table(
        name = "subscription", schema = "public",
        uniqueConstraints = [UniqueConstraint(columnNames = ["from_user_id", "to_user_id"])]
)
data class SubscriptionEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long,

        @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
        val fromUser: UserEntity,

        @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
        val toUser: UserEntity
)

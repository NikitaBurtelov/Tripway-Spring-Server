package tripway.server.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "trip", schema = "public")
data class Trip(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "tripname")
        val tripname: String,

        @JsonProperty("is_completed")
        @Column(name = "is_completed")
        val isCompleted: Boolean = false,

        @JsonProperty("first_point_name")
        @Column(name = "first_point_name")
        val firstPointName: String,

        @JsonProperty("last_point_name")
        @Column(name = "last_point_name")
        val lastPointName: String,

        //todo в будущем сделать photo not nullable. UPD: из-за того что сначала сохраняем трип, а потом только фото грузим на бек
        @Column(name = "photo")
        val photo: String? = null,

        @Column(name = "updated")
        val updated: Timestamp,

        @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
        val user: UserEntity
)

package tripway.server.models

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "point", schema = "public")
//@TypeDef(name = "list-array", typeClass = ListArrayType::class)
data class PointEntity(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "name")
        val name: String,

        @Column(name = "description")
        val description: String?,

//        //todo лучше вынести это в отдельную сущность с FK point_id
//        @Type(type = "list-array")
//        @Column(name = "photos", columnDefinition = "text[]")
//        val photos: List<String>? = null,

        @Column(name = "lat")
        val lat: Double,

        @Column(name = "lng")
        val lng: Double,

        @Column(name = "address")
        val address: String,

        @Column(name = "address_components", columnDefinition = "text")
        val addressComponents: String,

        @Column(name = "created")
        val created: Timestamp,

        @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
        val trip: Trip
)

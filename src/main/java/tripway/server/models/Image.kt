package tripway.server.models

import javax.persistence.*

@Entity
@Table(name = "image", schema = "public")
data class Image(
    @Id @Column(name = "id") val id: String = "",

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
    val pointEntity: PointEntity
)

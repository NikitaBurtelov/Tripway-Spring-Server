package tripway.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import tripway.server.models.Image

interface ImageRepository : JpaRepository<Image, Long>{
    @Modifying
    @Query("INSERT INTO image (id, point_entity_id) VALUES (:id, :point_id)", nativeQuery = true)
    @Transactional
    fun savePhoto(@Param("id") id: String, @Param("point_id") pointId: Long): Int

    fun findAllByPointEntity_Id(pointId: Long): List<Image>
}

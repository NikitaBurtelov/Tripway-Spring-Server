package tripway.server.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import tripway.server.models.PointEntity
import tripway.server.models.Trip
import java.util.*

interface PointRepository : JpaRepository<PointEntity, Long> {
//    @Modifying
//    @Transactional
//    @Query("UPDATE PointEntity u set u.photos = :uuid_photos where u.id = :point_id")
//    fun saveImages(@Param("uuid_photos") uuidPhotos: List<String>, @Param("point_id") pointId: Long)

    fun findByTrip_IdOrderByCreated(trip_id: Long): List<PointEntity>

    @Query(
        "SELECT u.id as userId, u.nickname as username,t.id as tripId, p.id as id, p.name as name, p.description as description, p.created as updated " +
                " FROM user_entity as u\n" +
                "    inner join trip t on u.id = t.user_id\n" +
                "    inner join point p on t.id = p.trip_id\n" +
                "where u.id in (:userIds) --#pageable\n",
        countQuery = "SELECT count(*) FROM user_entity as u\n" +
                "    inner join trip t on u.id = t.user_id\n" +
                "    inner join point p on t.id = p.trip_id\n" +
                "where u.id in (:userIds)",
        nativeQuery = true
    )
    fun findPointsByUserIdsPageable(@Param("userIds") userIds: Set<String>, pageable: Pageable): Slice<PointDtoProjection>
}

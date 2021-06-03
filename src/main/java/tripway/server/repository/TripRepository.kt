package tripway.server.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import tripway.server.models.Trip
import tripway.server.models.UserEntity

interface TripRepository : JpaRepository<Trip, Long> {
    fun findDistinctByUser_IdIn(userIds: List<String>, pageable: Pageable): Slice<Trip>

    fun findByUser_Id(userId: String): List<Trip>
}

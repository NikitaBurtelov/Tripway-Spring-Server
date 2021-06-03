package tripway.server.service.trips

import org.apache.catalina.User
import org.springframework.data.domain.Slice
import tripway.server.models.PointEntity
import tripway.server.models.Trip
import tripway.server.models.api.PointHomeFeedResponse
import tripway.server.models.api.PointRequest
import tripway.server.models.api.TripPageResponse

interface TripService {
    fun trip(id : Long) : Trip
    fun getTripById(tripId: Long): TripPageResponse
    fun getTripsRecomendatedDiscovery(uid: String, anchor: String): Slice<Trip>
    fun getPointsHomeFeed(uid: String, anchor: String): Slice<PointHomeFeedResponse.PointDto>
    fun savePoint(uid: String, point: PointRequest): PointEntity
    fun removeTrip(trip: Trip)
    fun removeTrip(id: Long)
    fun updateTrip(user: User)
    fun updateTrip(id: Long)
    fun getOwnTrips(uid: String): List<Trip>

    val PAGE_SIZE: Int
        get() = 25
}

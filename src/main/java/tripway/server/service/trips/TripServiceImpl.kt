package tripway.server.service.trips

import lombok.extern.slf4j.Slf4j
import org.apache.catalina.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import tripway.server.exceptions.ApiException
import tripway.server.exceptions.ApiException.Type
import tripway.server.models.PointEntity
import tripway.server.models.Trip
import tripway.server.models.api.*
import tripway.server.repository.*
import tripway.server.service.FileService
import java.sql.Timestamp
import java.time.Instant

@Slf4j
@Service
class TripServiceImpl : TripService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var pointRepository: PointRepository

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    private lateinit var fileService: FileService

    override fun getTripById(tripId: Long): TripPageResponse {
        val trip = tripRepository.findByIdOrNull(tripId)
            ?: throw ApiException(Type.TripDoesntExist)

        val points = pointRepository.findByTrip_IdOrderByCreated(tripId)
            .map {
                TripPageResponse.TripPoint(
                    it.id,
                    it.name,
                    it.description,
                    imageRepository.findAllByPointEntity_Id(it.id).map {photo->
                        fileService.getImageUrlByUUID(photo.id).toExternalForm()
                    },
                    it.created,
                    it.lat,
                    it.lng,
                    it.address,
                    it.addressComponents
                )
        }
        return trip.mapToResponseEntity(points)
    }

    override fun getTripsRecomendatedDiscovery(uid: String, anchor: String): Slice<Trip> {
        //todo 1. call to faiss
        // 2.getting trips
        val users = userRepository.findAll().map { it.id }.filter { it != uid }
        val pageRequest = PageRequest.of(anchor.toInt(), PAGE_SIZE, Sort.by("updated"))
        //todo 3. Selecting trips by some criteria
        return tripRepository
            .findDistinctByUser_IdIn(users, pageRequest)
            .map { trip ->
                trip.copy(photo = fileService.getImageUrlByUUID(trip.photo!!).toExternalForm())
            }
    }

    override fun getPointsHomeFeed(uid: String, anchor: String): Slice<PointHomeFeedResponse.PointDto> {
        val users = subscriptionRepository.findAllByFromUser_Id(uid)
            .map { it.toUser.id }
            .toSet()

        val pageRequest = PageRequest.of(anchor.toInt(), PAGE_SIZE, Sort.by("p.created"))
        return pointRepository
            .findPointsByUserIdsPageable(users, pageRequest)
            .map { point ->
                PointHomeFeedResponse.PointDto(
                    id = point.id,
                    name = point.name,
                    description = point.description,
                    photo = imageRepository.findAllByPointEntity_Id(point.id).last().let {
                        fileService.getImageUrlByUUID(it.id).toExternalForm()
                    },
                    updated = point.updated,
                    username = point.username,
                    userId = point.userId,
                    tripId = point.tripId
                )
            }
    }

    @Throws(ApiException::class)
    override fun savePoint(uid: String, point: PointRequest): PointEntity {
        val user = userRepository.findById(uid)
        val timestamp = Timestamp.from(Instant.now())

        if (user.isPresent) {
            val trip = if (point.tripId == null && point.tripName != null) {
                val newTrip = Trip(
                    tripname = point.tripName,
                    firstPointName = point.name,
                    lastPointName = point.name,
                    updated = timestamp,
                    user = user.get()
                )
                tripRepository.save(newTrip)
            } else if (point.tripId != null) {
                val existedTrip = tripRepository.findById(point.tripId).orElseGet { null }
                    ?: throw ApiException(Type.PointDoesntExist)
                tripRepository.save(existedTrip.copy(lastPointName = point.name, updated = timestamp))
                existedTrip
            } else throw ApiException(Type.BadRequest)

            val newPoint = PointEntity(
                name = point.name,
                description = point.description,
                created = timestamp,
                trip = trip,
                lat = point.lat,
                lng = point.lng,
                address = point.address,
                addressComponents = point.addressComponents
            )
            return pointRepository.save(newPoint)
        } else {
            throw ApiException(Type.UserDoesntExist)
        }
    }

    override fun getOwnTrips(uid: String): List<Trip> {
        //todo избавиться от избыточной информации о пользователе как FK поле user
        return tripRepository.findByUser_Id(uid)
            .map { trip ->
                trip.copy(photo = fileService.getImageUrlByUUID(trip.photo!!).toExternalForm())
            }
    }

    override fun trip(id: Long): Trip {
        return tripRepository.findById(id).get()
    }

    override fun removeTrip(trip: Trip) {
        tripRepository.delete(trip)
    }

    override fun removeTrip(id: Long) {
        tripRepository.deleteById(id)
    }

    override fun updateTrip(user: User) {}
    override fun updateTrip(id: Long) {}
}

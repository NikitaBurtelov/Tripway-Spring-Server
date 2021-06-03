package tripway.server.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tripway.server.FirebaseAppAdmin
import tripway.server.exceptions.ApiException
import tripway.server.exceptions.ApiException.Type
import tripway.server.models.api.PointHomeFeedResponse
import tripway.server.models.api.PointPostResponse
import tripway.server.models.api.PointRequest
import tripway.server.models.api.TripsDiscoveryResponse
import tripway.server.models.api.TripsDiscoveryResponse.Companion.mapToListDto
import tripway.server.service.FileService
import tripway.server.service.trips.TripServiceImpl

@RestController
@RequestMapping("/api/v1")
class TripController {

    @Autowired
    private lateinit var tripService: TripServiceImpl

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/discovery", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun tripsDiscovery(
        @RequestHeader("Token-ID") token: String,
        @RequestParam("anchor", defaultValue = "0") anchor: String
    ): ResponseEntity<TripsDiscoveryResponse> {
        //todo Алгоритм получения рекомендаций
        //1. Получаем пользователей схожих по интересам с данным пользователем
        //2. Получаем все трипы этих пользователей
        //3. В service применяем алгоритм ранжировки и выборки наиболее релевантных трипов
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        val tripsSlice = tripService.getTripsRecomendatedDiscovery(uid, anchor)
        if (!tripsSlice.hasContent()) {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.ok(
            TripsDiscoveryResponse(
                tripsSlice.content.mapToListDto(),
                tripsSlice.hasNext(),
                (tripsSlice.number + 1).toString()
            )
        )
    }

    @GetMapping("/homeFeed", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun homeFeed(
        @RequestHeader("Token-ID") token: String,
        @RequestParam("anchor", defaultValue = "0") anchor: String
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        val pointsSlice = tripService.getPointsHomeFeed(uid, anchor)
        if (!pointsSlice.hasContent()) {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.ok(
            PointHomeFeedResponse(
                pointsSlice.content,
                pointsSlice.hasNext(),
                (pointsSlice.number + 1).toString()
            )
        )
    }

    @PostMapping("/point", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun savePoint(
        @RequestHeader("Token-ID") token: String,
        @RequestBody point: PointRequest
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        return try {
            val savedPoint = tripService.savePoint(uid, point)

            ResponseEntity.ok(PointPostResponse(savedPoint.id))
        } catch (e: ApiException) {
            ResponseEntity(
                when (e.type) {
                    //todo добавить коды ошибок, чтобы идентифицировать на клиенте однозначно
                    //todo пока можно отправлять текст ошибки на русском
                    Type.AlreadyExistException -> "AlreadyExisted"
                    Type.BadRequest -> "Unacceptable tripId or tripName"
                    else -> "UNKNOWN ERROR"
                }, HttpStatus.BAD_REQUEST
            )
        }
    }

    @PostMapping("/uploadPointPhotos")
    fun savePointPhotos(
        @RequestParam("photos") files: List<MultipartFile>,
        @RequestParam("point_id") pointId: String
    ): ResponseEntity<Any> {
        return try {
            fileService.save(files, pointId.toLong())
            ResponseEntity.ok().build()
        } catch (e: ApiException) {
            when (e.type) {
                Type.PointDoesntExist -> ResponseEntity(HttpStatus.BAD_REQUEST)
                else -> ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
    }

    @GetMapping("ownTrips")
    fun getOwnTrips(@RequestHeader("Token-ID") token: String): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity.ok(tripService.getOwnTrips(uid))
    }

    @GetMapping("trip/{id}")
    fun getTripById(
        @RequestHeader("Token-ID") token: String,
        @PathVariable id: Long
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return try {
            ResponseEntity.ok(tripService.getTripById(id))
        } catch (e: Exception) {
            ResponseEntity(
                if (e is ApiException) {
                    when (e.type) {
                        Type.TripDoesntExist -> "Trip with such trip_id doesnt exist"
                        else -> "UNKNOWN ERROR"
                    }
                } else "UNKNOWN ERROR",
                HttpStatus.BAD_REQUEST
            )
        }
    }

    @GetMapping("/home")
    fun tripsHome(): String {
        return ""
    }

    @DeleteMapping("/trip")
    fun removeTripById(
        @RequestHeader("Token-ID") token: String,
        @RequestParam("trip_id") tripId: Long
    ): ResponseEntity<String>? {
        val uid = FirebaseAppAdmin.validateAndGetUID(token) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        return if (tripId < 0) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } else {
            val userId = tripService.trip(tripId).user.id

            if (uid == userId) {
                val trip = tripService.trip(tripId)

                tripService.removeTrip(tripId)
                fileService.removeImg(trip.photo!!)

                ResponseEntity<String>(HttpStatus.OK)

            }
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("trip")
    fun updateTrip(): String? {
        return null
    }
}

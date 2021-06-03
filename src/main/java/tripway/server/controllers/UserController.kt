package tripway.server.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tripway.server.FirebaseAppAdmin
import tripway.server.exceptions.ApiException
import tripway.server.models.UserEntity
import tripway.server.models.api.CountryInquirerRequestDto
import tripway.server.models.api.ProfileResponse
import tripway.server.models.api.ProfileResponse.Companion.mapToListDto
import tripway.server.service.trips.TripService
import tripway.server.service.users.UserService

@RestController
@RequestMapping("api/v1")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tripsService: TripService

    @GetMapping("/user/test")
    fun testMapping(): String {
        return "/index.html"
    }

    @GetMapping(value = ["/profile"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getProfile(
        @RequestHeader("Token-ID") token: String,
        @RequestParam(value = "user_id") requestedUserId: String?
    ): ResponseEntity<ProfileResponse> { //TODO token
        val ownId = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        val userId = requestedUserId ?: ownId

        val user = userService.getUserById(userId)
        val usersTrips = tripsService.getOwnTrips(userId).mapToListDto()
        val subscribersCount = userService.getSubscribersCount(userId)
        val subscriptionsCount = userService.getSubscriptionsCount(userId)

        val isRequestingUserIsSubscribedToRequestedUser =
            if (requestedUserId != null) {
                userService.isSubscribed(ownId, requestedUserId)
            } else false

        return ResponseEntity.ok(
            ProfileResponse(
                user.id,
                usersTrips,
                user.nickname,
                subscribersCount,
                subscriptionsCount,
                user.id == ownId,
                isRequestingUserIsSubscribedToRequestedUser
            )
        )
    }

    @GetMapping(value = ["/user/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserId(@PathVariable id: String): ResponseEntity<UserEntity> { //TODO token
        if (id == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val user = userService.getUserById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping(value = ["/subscribe"])
    fun subscribe(
        @RequestHeader("Token-ID") token: String,
        @RequestParam("user_id") userId: String
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity("token is invalid", HttpStatus.BAD_REQUEST)

        if (uid == userId) return ResponseEntity("Cant's subscribe on yourself!", HttpStatus.BAD_REQUEST)

        return try {
            userService.subscribeUser(uid, userId)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity(
                if (e is ApiException) {
                    when (e.type) {
                        ApiException.Type.UserAlreadySubscribed -> "User already subscribed"
                        else -> "UNKNOWN ERROR"
                    }
                } else "UNKNOWN ERROR",
                HttpStatus.BAD_REQUEST
            )
        }
    }

    @PostMapping(value = ["/unsubscribe"])
    fun unsubscribe(
        @RequestHeader("Token-ID") token: String,
        @RequestParam("user_id") userId: String
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity("token is invalid", HttpStatus.BAD_REQUEST)

        if (uid == userId) return ResponseEntity("Cant's unsubscribe from yourself!", HttpStatus.BAD_REQUEST)

        return try {
            userService.unsubscribeUser(uid, userId)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity(
                if (e is ApiException) {
                    when (e.type) {
                        ApiException.Type.UserIsntSubscribed -> "User isn't subscribed"
                        else -> "UNKNOWN ERROR"
                    }
                } else "UNKNOWN ERROR",
                HttpStatus.BAD_REQUEST
            )
        }
    }

    @PostMapping(value = ["/createUser"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun saveUser(@ModelAttribute user: UserEntity, @RequestHeader("Token-ID") token: String):
            ResponseEntity<Any> {
        if (user.email.isBlank() or user.nickname.isBlank() or user.password.isBlank()) {
            return ResponseEntity("Fields are empty", HttpStatus.BAD_REQUEST)
        }
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity("token is invalid", HttpStatus.BAD_REQUEST)

        userService.saveUser(user.copy(id = uid))
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @DeleteMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeUser(@PathVariable id: String?): ResponseEntity<UserEntity> {
        if (id == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        userService.removeUser(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping(value = ["user"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUser(@RequestBody user: UserEntity?): ResponseEntity<UserEntity> {
        if (user == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        userService.saveUser(user)
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @PostMapping(value = ["submitCountriesInquirer"])
    fun submitCountriesInquirer(
        @RequestBody countries: List<CountryInquirerRequestDto>,
        @RequestHeader("Token-ID") token: String
    ): ResponseEntity<Any> {
        val uid = FirebaseAppAdmin.validateAndGetUID(token)
            ?: return ResponseEntity("token is invalid", HttpStatus.BAD_REQUEST)

        return ResponseEntity.ok().build()
    }
}

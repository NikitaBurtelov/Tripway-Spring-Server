package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty
import tripway.server.models.Trip
import java.sql.Timestamp

data class ProfileResponse(
    val id: String,
    val trips: List<TripDto>,
    val nickname: String,

    @JsonProperty("subscribers_count")
    val subscribersCount: Int,

    @JsonProperty("subscriptions_count")
    val subscriptionsCount: Int,

    @JsonProperty("is_own")
    val ownProfile: Boolean,

    @JsonProperty("is_subscription")
    val subscription: Boolean
//        val avatar: String
) {
    data class TripDto(
        val id: Long,
        val tripname: String,

        @JsonProperty("is_completed")
        val isCompleted: Boolean,

        @JsonProperty("first_point_name")
        val firstPointName: String,

        @JsonProperty("last_point_name")
        val lastPointName: String,

        val photo: String,

        val updated: Timestamp
    )

    companion object {
        fun List<Trip>.mapToListDto() = map {
            TripDto(
                it.id,
                it.tripname,
                it.isCompleted,
                it.firstPointName,
                it.lastPointName,
                it.photo!!,
                it.updated
            )
        }
    }
}

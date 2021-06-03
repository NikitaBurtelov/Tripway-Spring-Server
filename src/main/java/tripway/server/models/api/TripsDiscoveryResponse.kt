package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.Column

data class TripsDiscoveryResponse(
    val trips: List<TripDto>,

    @JsonProperty("has_more")
    val hasMore: Boolean,

    val anchor: String
) {
    data class TripDto(
        val id: Long,

        @Column(name = "tripname")
        val tripname: String,

        @JsonProperty("is_completed")
        val isCompleted: Boolean,

        @JsonProperty("first_point_name")
        val firstPointName: String,

        @JsonProperty("last_point_name")
        val lastPointName: String,

        val photo: String,

        val updated: Timestamp,

        @JsonProperty("user_id")
        val userId: String,

        @JsonProperty("user_name")
        val userName: String
    )

    companion object {
        fun List<tripway.server.models.Trip>.mapToListDto() = map {
            TripDto(
                it.id,
                it.tripname,
                it.isCompleted,
                it.firstPointName,
                it.lastPointName,
                it.photo!!,
                it.updated,
                it.user.id,
                it.user.nickname
            )
        }
    }
}

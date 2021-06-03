package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty
import tripway.server.models.PointEntity
import tripway.server.models.Trip
import java.sql.Timestamp

data class TripPageResponse(
        val id: Long,
        val tripname: String,

        @JsonProperty("is_completed")
        val isCompleted: Boolean,

        @JsonProperty("first_point_name")
        val firstPointName: String,

        @JsonProperty("last_point_name")
        val lastPointName: String,

        //todo в будущем сделать photo not nullable. UPD: из-за того что сначала сохраняем трип, а потом только фото грузим на бек
        val photo: String,

        val updated: Timestamp,

        val points: List<TripPoint>
) {
    data class TripPoint(
            val id: Long,
            val name: String,
            val description: String?,
            val photos: List<String>,
            val created: Timestamp,
            @JsonProperty("lat") val lat: Double,
            @JsonProperty("lng") val lng: Double,
            @JsonProperty("address") val address: String,
            @JsonProperty("address_components") val addressComponents: String
    )
}

fun Trip.mapToResponseEntity(points: List<TripPageResponse.TripPoint>) =
        TripPageResponse(
                id,
                tripname,
                isCompleted,
                firstPointName,
                lastPointName,
                photo!!,
                updated,
                points
        )

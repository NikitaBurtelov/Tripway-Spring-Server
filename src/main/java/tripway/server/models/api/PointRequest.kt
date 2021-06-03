package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty

data class PointRequest(
        val id: Long,
        val name: String,
        val description: String?,
        @JsonProperty("trip_name")
        val tripName: String?,
        @JsonProperty("trip_id")
        val tripId: Long?,
        val lat: Double,
        val lng: Double,
        val address: String,
        @JsonProperty("address_components")
        val addressComponents: String
)

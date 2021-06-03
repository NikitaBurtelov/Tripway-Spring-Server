package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty
import tripway.server.models.PointEntity
import java.sql.Timestamp
import javax.persistence.Column

data class PointHomeFeedResponse(
    val points: List<PointDto>,
    @JsonProperty("has_more")
    val hasMore: Boolean,
    val anchor: String
) {
    data class PointDto(
        @JsonProperty("id") val id: Long,
        @JsonProperty("name") val name: String,
        @JsonProperty("description") val description: String?,
        @JsonProperty("photo") val photo: String,
        @JsonProperty("updated") val updated: Timestamp,
        @JsonProperty("username") val username: String,
        @JsonProperty("user_id") val userId: String,
        @JsonProperty("trip_id") val tripId: Long
    )
}

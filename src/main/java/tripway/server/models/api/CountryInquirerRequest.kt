package tripway.server.models.api

import com.fasterxml.jackson.annotation.JsonProperty

data class CountryInquirerRequestDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("selected") val selected: Boolean
)

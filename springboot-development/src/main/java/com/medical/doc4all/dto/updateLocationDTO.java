package com.medical.doc4all.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class updateLocationDTO {
    @JsonAlias({"longitude", "Longitude", "long", "Long"})
    private Double Longitude;
    @JsonAlias({"latitude", "Latitude", "lat", "Lat"})
    private Double Latitude;
}

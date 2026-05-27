package com.medical.doc4all.repository.mapper;



import com.medical.doc4all.dto.DispensaryCreateRequest;
import com.medical.doc4all.dto.DispensaryResponse;
import com.medical.doc4all.entity.Dispensary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DispensaryMapper {

    public Dispensary toEntity(DispensaryCreateRequest request) {
        return Dispensary.builder()
                .name(request.getName())
                .email(request.getEmail())

              //  .address(request.getAddress())

              //  .licenseNumber(request.getLicenseNumber())

              //  .isValid(false) // Default to false, admin needs to approve
                //.createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public DispensaryResponse toResponse(Dispensary dispensary) {
        return DispensaryResponse.builder()
                .id(dispensary.getId())
                .name(dispensary.getName())
                .email(dispensary.getEmail())
                .review_count(dispensary.getReviewCount())
                .address(dispensary.getAddress())
                .description(dispensary.getDescription())
                .type(String.valueOf(dispensary.getType()))
                .longitude(dispensary.getLongitude())
                .latitude(dispensary.getLatitude())
                .rating(dispensary.getRating())

              //  .licenseNumber(dispensary.getLicenseNumber())

               // .isValid(dispensary.getIsValid())
                //.createdAt(dispensary.getCreatedAt())
               // .updatedAt(dispensary.getUpdatedAt())
                .build();
    }
}

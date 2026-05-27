package com.medical.doc4all.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewableDTO {
    private boolean Doctorreviewable;
    private boolean Dispensaryreviewable;

}

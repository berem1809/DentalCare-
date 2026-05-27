package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRespondDTO {
    private Long invitationId;
    private String response; // "ACCEPT" or "REJECT"
    // getters / setters
}

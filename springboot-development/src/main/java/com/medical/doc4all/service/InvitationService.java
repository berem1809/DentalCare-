package com.medical.doc4all.service;

import com.medical.doc4all.dto.DoctorInvitationRequestDTO;
import com.medical.doc4all.dto.DoctorInvitationResponseDTO;
import com.medical.doc4all.dto.InvitationRespondDTO;

import java.util.List;

public interface InvitationService {
    DoctorInvitationResponseDTO createInvitation(DoctorInvitationRequestDTO dto, String email);
    DoctorInvitationResponseDTO respondToInvitation(InvitationRespondDTO dto);
    List<DoctorInvitationResponseDTO> getInvitationsForDoctor(String email);
    List<DoctorInvitationResponseDTO> getInvitationsForDispensary(String email);

    void revokeInvitation(Long invitationId);
}


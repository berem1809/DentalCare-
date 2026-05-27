package com.medical.doc4all.service;

import com.medical.doc4all.dto.SlotDTO;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {
    List<SlotDTO> getAvailableSlots(Long scheduleId, LocalDate date);

}


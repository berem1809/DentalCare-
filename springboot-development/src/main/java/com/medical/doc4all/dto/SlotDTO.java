package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class SlotDTO {
    private LocalTime start;
    private LocalTime end;
    private boolean available;

    public SlotDTO(LocalTime start, LocalTime end, boolean available) {
        this.start = start;
        this.end = end;
        this.available = available;
    }
    // getters/setters
}


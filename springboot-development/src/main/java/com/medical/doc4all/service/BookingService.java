package com.medical.doc4all.service;

import com.medical.doc4all.dto.BookingRequestDTO;
import com.medical.doc4all.dto.BookingResponseDTO;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO dto, String email);
    // you can add cancel, list etc.
    BookingResponseDTO cancelBooking(Long bookingId, boolean refundRequested);

    Iterable<BookingResponseDTO> getBookings(String email);
}

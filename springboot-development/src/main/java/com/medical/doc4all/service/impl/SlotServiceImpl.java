package com.medical.doc4all.service.impl;

import com.medical.doc4all.dto.SlotDTO;
import com.medical.doc4all.entity.Booking;
import com.medical.doc4all.entity.DoctorSchedule;
import com.medical.doc4all.enums.BookingStatus;
import com.medical.doc4all.enums.PaymentStatus;
import com.medical.doc4all.exception.BadRequestException;
import com.medical.doc4all.exception.ResourceNotFoundException;
import com.medical.doc4all.repository.BookingRepository;
import com.medical.doc4all.repository.DoctorScheduleRepository;
import com.medical.doc4all.service.SlotService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {

    private final DoctorScheduleRepository scheduleRepo;
    private final BookingRepository bookingRepo;

    @Autowired
    public SlotServiceImpl(DoctorScheduleRepository scheduleRepo, BookingRepository bookingRepo) {
        this.scheduleRepo = scheduleRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    @Transactional
    public List<SlotDTO> getAvailableSlots(Long scheduleId, LocalDate date) {
        DoctorSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));

        // ensure the date matches the weekly schedule's day
        if (schedule.getDay() != date.getDayOfWeek()) {
            // could also return empty list; throwing bad request is clearer for client
            throw new BadRequestException("Date " + date + " does not fall on schedule day " + schedule.getDay());
        }

        // fetch booked start times for the schedule & date
        List<Booking> bookings = bookingRepo.findByScheduleIdAndAppointmentDate(scheduleId, date);
//        Set<LocalTime> bookedStarts = bookings.stream()
//                .map(Booking::getSlotStartTime)
//                .collect(Collectors.toSet());

        Set<LocalTime> bookedStarts = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED
                        && b.getPaymentStatus() == PaymentStatus.PAID)
                .map(Booking::getSlotStartTime)
                .collect(Collectors.toSet());

        List<SlotDTO> slots = new ArrayList<>();

        LocalTime t = schedule.getStartTime();
        int step = schedule.getPerPatientMinutes();

        // Use condition: slot end must be <= schedule end.
        while (!t.plusMinutes(step).isAfter(schedule.getEndTime())) {
            LocalTime slotEnd = t.plusMinutes(step);
            boolean available = !bookedStarts.contains(t);
            slots.add(new SlotDTO(t, slotEnd, available));
            t = t.plusMinutes(step);
        }

        return slots;
    }


    private boolean timesOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }
}


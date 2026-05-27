package com.medical.doc4all.service.impl;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendBookingConfirmed(String patientEmail, String doctorName) throws MessagingException {
        String subject = "Booking Confirmed!";
        String body = String.format("Your booking with Dr. %s has been confirmed.", doctorName);
        emailService.sendEmail(patientEmail, subject, body);
    }

    public void sendBookingRefunded(String patientEmail, String transactionId) throws MessagingException {
        String subject = "Refund Processed";
        String body = String.format("Your booking has been cancelled and refunded. Transaction ID: %s", transactionId);
        emailService.sendEmail(patientEmail, subject, body);
    }

    public void sendDoctorScheduleConfirmed(String doctorEmail, String scheduleDate) throws MessagingException {
        String subject = "Schedule Approved";
        String body = String.format("Your schedule on %s has been confirmed.", scheduleDate);
        emailService.sendEmail(doctorEmail, subject, body);
    }

    public void sendDoctorScheduleUnconfirmed(String doctorEmail, String scheduleDate) throws MessagingException {
        String subject = "Schedule Rejected";
        String body = String.format("Your schedule on %s has been unconfirmed.", scheduleDate);
        emailService.sendEmail(doctorEmail, subject, body);
    }

    public void sendReportShared(String doctorEmail, String patientName) throws MessagingException {
        String subject = "Medical Report Shared";
        String body = String.format("Patient %s has shared their medical report with you.", patientName);
        emailService.sendEmail(doctorEmail, subject, body);
    }

    public void sendReportAccessRevoked(String doctorEmail, String patientName) throws MessagingException {
        String subject = "Report Access Revoked";
        String body = String.format("Patient %s has revoked your access to their medical report.", patientName);
        emailService.sendEmail(doctorEmail, subject, body);
    }
}


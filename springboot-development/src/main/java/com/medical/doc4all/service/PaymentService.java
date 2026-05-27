package com.medical.doc4all.service;

import com.medical.doc4all.exception.PaymentException;

public interface PaymentService {
    /**
     * Creates a sale transaction and returns the transaction id.
     * Throws PaymentException on failure.
     */
    String charge(double amount, String paymentMethodNonce) throws PaymentException;

    /**
     * Refunds (or voids) a transaction. Returns true if refund/void succeeded.
     */
    String refund(String transactionId) throws PaymentException;
}


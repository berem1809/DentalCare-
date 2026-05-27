package com.medical.doc4all.service.impl;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.medical.doc4all.exception.PaymentException;
import com.medical.doc4all.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BraintreePaymentService implements PaymentService {

    private final BraintreeGateway gateway;

    @Autowired
    public BraintreePaymentService(BraintreeGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public String charge(double amount, String paymentMethodNonce) throws PaymentException {
        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal(String.valueOf(amount)))
                .paymentMethodNonce(paymentMethodNonce)
                .options()
                .submitForSettlement(true) // submit to settlement immediately
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction txn = result.getTarget();
            return txn.getId();
        } else {
            String message = "Payment failed";
            if (result.getMessage() != null) message += ": " + result.getMessage();
            // include processor errors if needed
            throw new PaymentException(message);
        }
    }

    @Override
    public String refund(String transactionId) throws PaymentException {
        try {
            // Try to void first (works if transaction is not yet settled).
            Result<Transaction> voidResult = gateway.transaction().voidTransaction(transactionId);
            if (voidResult != null && voidResult.isSuccess()) {
                Transaction txn = voidResult.getTarget();
                // void returns a transaction object (id usually same as original or returns new id depending on Braintree)
                return txn.getId();
            }

            // If cannot void, attempt refund
            Result<Transaction> refundResult = gateway.transaction().refund(transactionId);
            if (refundResult != null && refundResult.isSuccess()) {
                Transaction refundTxn = refundResult.getTarget();
                return refundTxn.getId();
            }

            // Try to extract useful errors
            String message = "Refund failed";
            if (refundResult != null && refundResult.getMessage() != null) message += ": " + refundResult.getMessage();
            throw new PaymentException(message);

        } catch (PaymentException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PaymentException("Refund failed: " + e.getMessage());
        }
    }


}


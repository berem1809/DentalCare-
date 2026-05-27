package com.medical.doc4all.controller;

import com.braintreegateway.BraintreeGateway;
import com.medical.doc4all.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final BraintreeGateway gateway;

    @Autowired
    public PaymentController(BraintreeGateway gateway) { this.gateway = gateway; }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> generateClientToken() {
        String clientToken = gateway.clientToken().generate();
        return ResponseEntity.ok(new TokenResponse(clientToken) );
    }
}


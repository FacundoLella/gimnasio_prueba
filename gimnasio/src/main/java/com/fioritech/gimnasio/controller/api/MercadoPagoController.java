package com.fioritech.gimnasio.controller.api;

import com.fioritech.gimnasio.business.logic.error.BusinessException;
import com.fioritech.gimnasio.business.logic.service.MercadoPagoService;
import com.fioritech.gimnasio.controller.api.dto.MercadoPagoPreferenceRequest;
import com.fioritech.gimnasio.controller.api.dto.MercadoPagoPreferenceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/preferences")
    public ResponseEntity<MercadoPagoPreferenceResponse> createPreference(
        @Valid @RequestBody MercadoPagoPreferenceRequest request) {
        MercadoPagoPreferenceResponse response = mercadoPagoService.createPreference(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

package com.hugo.poliza_api.controller;

import com.hugo.poliza_api.dto.CoreEventoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    @PostMapping("/evento")
    public ResponseEntity<String> registrarEvento(@RequestBody CoreEventoRequest request) {
        log.info("Mock CORE recibido -> evento: {}, polizaId: {}",
                request.getEvento(),
                request.getPolizaId());

        return ResponseEntity.ok("Evento registrado en logs");
    }

}

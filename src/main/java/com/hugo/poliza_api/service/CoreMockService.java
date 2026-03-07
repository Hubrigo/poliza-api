package com.hugo.poliza_api.service;

import com.hugo.poliza_api.dto.CoreEventoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreMockService {

    private final RestTemplate restTemplate;

    public void registrarEvento(String evento, Long polizaId) {

        log.info("Evento enviado al CORE -> evento: {}, polizaId: {}", evento, polizaId);

        CoreEventoRequest request = new CoreEventoRequest();
        request.setEvento(evento);
        request.setPolizaId(polizaId);

        restTemplate.postForObject(
                "http://localhost:8080/core-mock/evento",
                request,
                String.class
        );
    }

}

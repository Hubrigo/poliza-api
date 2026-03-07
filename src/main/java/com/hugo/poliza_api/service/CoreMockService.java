package com.hugo.poliza_api.service;

import com.hugo.poliza_api.dto.CoreEventoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreMockService {

    private final RestTemplate restTemplate;

    @Value("${app.api-key}")
    private String apiKey;

    public void registrarEvento(String evento, Long polizaId) {
        CoreEventoRequest request = new CoreEventoRequest();
        request.setEvento(evento);
        request.setPolizaId(polizaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        HttpEntity<CoreEventoRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForObject(
                "http://localhost:8080/core-mock/evento",
                entity,
                String.class
        );

        log.info("Evento enviado al CORE -> evento: {}, polizaId: {}", evento, polizaId);
    }
}

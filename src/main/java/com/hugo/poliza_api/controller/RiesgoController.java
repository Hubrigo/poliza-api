package com.hugo.poliza_api.controller;

import com.hugo.poliza_api.model.Riesgo;
import com.hugo.poliza_api.service.RiesgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riesgos")
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService riesgoService;

    @PostMapping("/{id}/cancelar")
    public Riesgo cancelarRiesgo(@PathVariable Long id) {
        return riesgoService.cancelarRiesgo(id);
    }

}

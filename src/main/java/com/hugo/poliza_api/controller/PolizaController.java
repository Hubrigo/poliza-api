package com.hugo.poliza_api.controller;

import com.hugo.poliza_api.model.Poliza;
import com.hugo.poliza_api.model.Riesgo;
import com.hugo.poliza_api.model.enums.EstadoPoliza;
import com.hugo.poliza_api.model.enums.TipoPoliza;
import com.hugo.poliza_api.service.PolizaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;

    @GetMapping
    public List<Poliza> listarPolizas(
            @RequestParam(required = false) TipoPoliza tipo,
            @RequestParam(required = false) EstadoPoliza estado
    ) {
        return polizaService.listar(tipo, estado);
    }

    @GetMapping("/{id}/riesgos")
    public List<Riesgo> listarRiesgosPorPoliza(@PathVariable Long id) {
        return polizaService.listarRiesgosPorPoliza(id);
    }

    @PostMapping("/{id}/renovar")
    public Poliza renovarPoliza(@PathVariable Long id) {
        return polizaService.renovarPoliza(id);
    }

    @PostMapping("/{id}/cancelar")
    public Poliza cancelarPoliza(@PathVariable Long id) {
        return polizaService.cancelarPoliza(id);
    }

    @PostMapping("/{id}/riesgos")
    public Riesgo agregarRiesgo(@PathVariable Long id, @RequestBody Riesgo riesgo) {
        return polizaService.agregarRiesgo(id, riesgo);
    }

}

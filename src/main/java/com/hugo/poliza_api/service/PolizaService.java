package com.hugo.poliza_api.service;

import com.hugo.poliza_api.exception.BusinessException;
import com.hugo.poliza_api.model.Poliza;
import com.hugo.poliza_api.model.Riesgo;
import com.hugo.poliza_api.model.enums.EstadoPoliza;
import com.hugo.poliza_api.model.enums.EstadoRiesgo;
import com.hugo.poliza_api.model.enums.TipoPoliza;
import com.hugo.poliza_api.repository.PolizaRepository;
import com.hugo.poliza_api.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolizaService {

    private static final BigDecimal IPC = new BigDecimal("0.10");

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final CoreMockService coreMockService;

    public List<Poliza> listar(TipoPoliza tipo, EstadoPoliza estado) {
        if (tipo != null && estado != null) {
            return polizaRepository.findByTipoAndEstado(tipo, estado);
        }
        if (tipo != null) {
            return polizaRepository.findByTipo(tipo);
        }
        if (estado != null) {
            return polizaRepository.findByEstado(estado);
        }
        return polizaRepository.findAll();
    }

    public List<Riesgo> listarRiesgosPorPoliza(Long polizaId) {
        validarPolizaExiste(polizaId);
        return riesgoRepository.findByPolizaId(polizaId);
    }

    public Poliza renovarPoliza(Long polizaId) {
        Poliza poliza = obtenerPoliza(polizaId);

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException("No se puede renovar una póliza cancelada");
        }

        BigDecimal nuevoCanon = poliza.getCanonMensual()
                .multiply(BigDecimal.ONE.add(IPC))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal nuevaPrima = nuevoCanon
                .multiply(BigDecimal.valueOf(poliza.getVigenciaMeses()))
                .setScale(2, RoundingMode.HALF_UP);

        poliza.setCanonMensual(nuevoCanon);
        poliza.setPrima(nuevaPrima);
        poliza.setEstado(EstadoPoliza.RENOVADA);

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreMockService.registrarEvento("ACTUALIZACION", polizaGuardada.getId());

        return polizaGuardada;
    }

    public Poliza cancelarPoliza(Long polizaId) {
        Poliza poliza = obtenerPoliza(polizaId);

        poliza.setEstado(EstadoPoliza.CANCELADA);

        for (Riesgo riesgo : poliza.getRiesgos()) {
            riesgo.setEstado(EstadoRiesgo.CANCELADO);
        }

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreMockService.registrarEvento("ACTUALIZACION", polizaGuardada.getId());

        return polizaGuardada;
    }

    public Riesgo agregarRiesgo(Long polizaId, Riesgo riesgo) {
        Poliza poliza = obtenerPoliza(polizaId);

        if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
            throw new BusinessException("Solo las pólizas colectivas permiten agregar riesgos");
        }

        riesgo.setEstado(EstadoRiesgo.ACTIVO);
        riesgo.setPoliza(poliza);

        Riesgo riesgoGuardado = riesgoRepository.save(riesgo);
        coreMockService.registrarEvento("ACTUALIZACION", poliza.getId());

        return riesgoGuardado;
    }

    private Poliza obtenerPoliza(Long polizaId) {
        return polizaRepository.findById(polizaId)
                .orElseThrow(() -> new BusinessException("Póliza no encontrada con id: " + polizaId));
    }

    private void validarPolizaExiste(Long polizaId) {
        if (!polizaRepository.existsById(polizaId)) {
            throw new BusinessException("Póliza no encontrada con id: " + polizaId);
        }
    }

}

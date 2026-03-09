package com.hugo.poliza_api.service;

import com.hugo.poliza_api.exception.BusinessException;
import com.hugo.poliza_api.model.Riesgo;
import com.hugo.poliza_api.model.enums.EstadoRiesgo;
import com.hugo.poliza_api.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiesgoService {

    private final RiesgoRepository riesgoRepository;
    private final CoreMockService coreMockService;

    public Riesgo cancelarRiesgo(Long riesgoId) {
        Riesgo riesgo = riesgoRepository.findById(riesgoId)
                .orElseThrow(() -> new BusinessException("Riesgo no encontrado con id: " + riesgoId));

        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        Riesgo riesgoGuardado = riesgoRepository.save(riesgo);

        coreMockService.registrarEvento("ACTUALIZACION", riesgoGuardado.getPoliza().getId());

        return riesgoGuardado;
    }

}

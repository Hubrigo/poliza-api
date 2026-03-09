package com.hugo.poliza_api.repository;

import com.hugo.poliza_api.model.Poliza;
import com.hugo.poliza_api.model.enums.EstadoPoliza;
import com.hugo.poliza_api.model.enums.TipoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolizaRepository extends JpaRepository<Poliza,Long> {

    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);

    List<Poliza> findByTipo(TipoPoliza tipo);

    List<Poliza> findByEstado(EstadoPoliza estado);

}

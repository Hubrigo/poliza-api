package com.hugo.poliza_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreEventoRequest {
    private String evento;
    private Long polizaId;
}

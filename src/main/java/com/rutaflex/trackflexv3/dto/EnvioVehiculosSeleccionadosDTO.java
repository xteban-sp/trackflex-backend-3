
package com.rutaflex.trackflexv3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioVehiculosSeleccionadosDTO {
    private List<String> destinatarios;
    private List<Long> vehiculoIds; // Asume que tu ID es Long
}
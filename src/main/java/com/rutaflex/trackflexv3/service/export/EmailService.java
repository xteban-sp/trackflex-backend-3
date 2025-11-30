// src/main/java/com/rutaflex/trackflexv3/service/EmailService.java
package com.rutaflex.trackflexv3.service.export;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void enviarVehiculosDisponibles(List<VehiculoDTO> vehiculos, List<String> destinatarios, LocalDate fecha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatarios.toArray(new String[0]));
        message.setSubject("Vehículos Disponibles - " + fecha);
        message.setText(construirContenido(vehiculos, fecha));
        emailSender.send(message);
    }

    private String construirContenido(List<VehiculoDTO> vehiculos, LocalDate fecha) {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehículos disponibles para el ").append(fecha).append(":\n\n");
        sb.append(String.format("%-10s %-15s %-15s %-8s %-15s\n", "PLACA", "MARCA", "MODELO", "NIVEL", "CAPACIDAD"));
        sb.append("--------------------------------------------------------\n");
        for (VehiculoDTO v : vehiculos) {
            sb.append(String.format("%-10s %-15s %-15s %-8s %-15s\n",
                    v.getPlaca(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getNivel().getCodigo(),
                    v.getNivel().getM3Min() + " - " + v.getNivel().getM3Max()
            ));
        }
        return sb.toString();
    }
}
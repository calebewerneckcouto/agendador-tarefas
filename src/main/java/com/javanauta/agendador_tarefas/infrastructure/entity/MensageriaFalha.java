package com.javanauta.agendador_tarefas.infrastructure.entity;

import com.javanauta.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("mensageria-falha")
public class MensageriaFalha {
    @Id
    private String id;
    private String topico;
    private String mensagem;
    private String stackTrace;
    private LocalDateTime dataFalha;


}

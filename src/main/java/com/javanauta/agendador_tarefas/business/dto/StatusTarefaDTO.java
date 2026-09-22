package com.javanauta.agendador_tarefas.business.dto;

import com.javanauta.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusTarefaDTO {

    private String id;
    private StatusNotificacao statusNotificacao;
}

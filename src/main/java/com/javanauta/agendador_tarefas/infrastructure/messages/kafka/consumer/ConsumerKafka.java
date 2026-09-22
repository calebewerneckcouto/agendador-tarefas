package com.javanauta.agendador_tarefas.infrastructure.messages.kafka.consumer;

import com.javanauta.agendador_tarefas.business.dto.StatusTarefaDTO;
import com.javanauta.agendador_tarefas.business.service.TarefasService;
import com.javanauta.agendador_tarefas.infrastructure.entity.MensageriaFalha;
import com.javanauta.agendador_tarefas.infrastructure.repository.MensageriaFalhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ConsumerKafka {

    private final ObjectMapper objectMapper;
    private final TarefasService tarefasService;
    private final MensageriaFalhaRepository mensageriaFalhaRepository;

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 3000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "status-notificacao", groupId = "grupo-agendador", containerFactory = "kafkaListenerContainerFactory")
    public void consumirStatusTarefa(String mensagem) {
        StatusTarefaDTO dto = objectMapper.readValue(mensagem, StatusTarefaDTO.class);
        tarefasService.alteraStatus(dto.getStatusNotificacao(), dto.getId());
    }


    @DltHandler
    public void recuperarMensagemFalha(String mensagem,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, Throwable throwable) {
        MensageriaFalha mensageriaFalha = MensageriaFalha.builder()
                .topico(topic)
                .dataFalha(LocalDateTime.now())
                .stackTrace(throwable.getMessage())
                .mensagem(mensagem)
                .build();

        mensageriaFalhaRepository.save(mensageriaFalha);
    }


}

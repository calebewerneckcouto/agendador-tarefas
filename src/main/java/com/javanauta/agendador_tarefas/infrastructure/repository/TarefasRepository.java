package com.javanauta.agendador_tarefas.infrastructure.repository;

import com.javanauta.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TarefasRepository extends MongoRepository<TarefasEntity,String> {

    List<TarefasEntity> findByDataEventoBetweenAndStatusNotificacao(LocalDateTime dataInicial, LocalDateTime dataFinal, StatusNotificacao status);

    List<TarefasEntity> findByEmailUsuario(String email);
}

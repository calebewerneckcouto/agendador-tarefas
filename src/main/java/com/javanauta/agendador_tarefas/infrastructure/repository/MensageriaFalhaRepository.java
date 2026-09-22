package com.javanauta.agendador_tarefas.infrastructure.repository;

import com.javanauta.agendador_tarefas.infrastructure.entity.MensageriaFalha;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensageriaFalhaRepository extends MongoRepository<MensageriaFalha,String> {
}

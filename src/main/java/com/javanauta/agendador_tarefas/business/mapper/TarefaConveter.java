package com.javanauta.agendador_tarefas.business.mapper;

import com.javanauta.agendador_tarefas.business.dto.TarefasDTO;
import com.javanauta.agendador_tarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaConveter {
    @Mapping(source = "id",target="id")
    @Mapping(source = "dataEvento",target = "dataEvento")
    @Mapping(source = "dataCriacao", target="dataCriacao")
    TarefasEntity paraTarefaEntity(TarefasDTO tarefasDTO);
    TarefasDTO paraTarefasDTO(TarefasEntity tarefasEntity);
    List<TarefasEntity>paraListaTarefasEntity(List<TarefasDTO> dtos);
    List<TarefasDTO> paraListaTarefasDTO(List<TarefasEntity>entities);
}

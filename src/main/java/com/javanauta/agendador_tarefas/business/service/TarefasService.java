package com.javanauta.agendador_tarefas.business.service;

import com.javanauta.agendador_tarefas.business.dto.TarefasDTO;
import com.javanauta.agendador_tarefas.business.mapper.TarefaConveter;
import com.javanauta.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.javanauta.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefaConveter tarefaConveter;
    private final JwtUtil jwtUtil;


    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {

        String email = jwtUtil.extractUsername(token.substring(7));
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatusNotificacao(StatusNotificacao.PENDENTE);
        tarefasDTO.setEmailUsuario(email);
        TarefasEntity entity = tarefaConveter.paraTarefaEntity(tarefasDTO);
        return tarefaConveter.paraTarefasDTO(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConveter.paraListaTarefasDTO(tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefaConveter.paraListaTarefasDTO(listaTarefas);
    }


}

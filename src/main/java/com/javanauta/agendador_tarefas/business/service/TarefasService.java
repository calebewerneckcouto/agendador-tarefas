package com.javanauta.agendador_tarefas.business.service;

import com.javanauta.agendador_tarefas.business.dto.TarefasDTO;
import com.javanauta.agendador_tarefas.business.mapper.TarefaConveter;
import com.javanauta.agendador_tarefas.business.mapper.TarefaUpdateConverter;
import com.javanauta.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.javanauta.agendador_tarefas.infrastructure.exception.ResourceNotFoundException;
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
    private final TarefaUpdateConverter tarefaUpdateConverter;


    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {

        String email = jwtUtil.extractUsername(jwtUtil.sanitizeToken(token));
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatusNotificacao(StatusNotificacao.PENDENTE);
        tarefasDTO.setEmailUsuario(email);
        TarefasEntity entity = tarefaConveter.paraTarefaEntity(tarefasDTO);
        return tarefaConveter.paraTarefasDTO(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConveter.paraListaTarefasDTO(tarefasRepository.findByDataEventoBetweenAndStatusNotificacao(dataInicial, dataFinal,StatusNotificacao.PENDENTE));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extractUsername(jwtUtil.sanitizeToken(token));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefaConveter.paraListaTarefasDTO(listaTarefas);
    }


    public void deletaTarefasPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro ao deletar" + id + e.getMessage());
        }


    }

    public TarefasDTO alteraStatus(StatusNotificacao status, String id) {

        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada" + id));
            tarefasEntity.setStatusNotificacao(status);
            return tarefaConveter.paraTarefasDTO(tarefasRepository.save(tarefasEntity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa" + e.getCause());
        }


    }

    public TarefasDTO updateTarefas(TarefasDTO dto,String id){
        try{
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada" + id));
            tarefaUpdateConverter.updateTarefas(dto,tarefasEntity);
            return tarefaConveter.paraTarefasDTO(tarefasRepository.save(tarefasEntity));
        }catch(ResourceNotFoundException e ){
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa"+ e.getCause());
        }
    }
}

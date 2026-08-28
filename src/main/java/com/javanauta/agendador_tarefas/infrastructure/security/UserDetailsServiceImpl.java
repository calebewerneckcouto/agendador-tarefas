package com.javanauta.agendador_tarefas.infrastructure.security;


import com.javanauta.agendador_tarefas.business.dto.UsuarioDTO;
import com.javanauta.agendador_tarefas.infrastructure.client.UsuarioClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl {
    @Autowired
    private UsuarioClient usuarioClient;

     public UserDetails carregaDadosUsuario(String email,String token){
        UsuarioDTO usuarioDTO = usuarioClient.buscaUsuarioPorEmail(email,token);

        return org.springframework.security.core.userdetails.User
                .withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha())
                .build();
    }
}

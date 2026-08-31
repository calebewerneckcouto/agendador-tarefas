package com.javanauta.agendador_tarefas.infrastructure.security;

import com.javanauta.agendador_tarefas.business.dto.UsuarioDTO;
import com.javanauta.agendador_tarefas.infrastructure.client.UsuarioClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioClient usuarioClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new UsernameNotFoundException("Requisição não disponível");
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        return carregaDadosUsuario(email, token);
    }

    public UserDetails carregaDadosUsuario(String email, String token) {
        UsuarioDTO usuarioDTO = usuarioClient.buscaUsuarioPorEmail(email, token);

        return User.withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha())
                .authorities("ROLE_USER")
                .build();
    }
}

package br.com.alura.forum.security.service;

import br.com.alura.forum.model.User;
import br.com.alura.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> algumUser = userRepository.findByEmail(email);
        return algumUser
                .orElseThrow(() ->
                        new UsernameNotFoundException("Não foi possivel encontrar usuário com email: " + email));
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> algumUser = userRepository.findById(id);
        return algumUser
                .orElseThrow(() ->
                        new UsernameNotFoundException("Não foi possivel encontrar usuário com id: " + id));
    }
}

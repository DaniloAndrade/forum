package br.com.alura.forum;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteMain {

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }
}

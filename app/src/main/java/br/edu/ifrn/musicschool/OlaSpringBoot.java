package br.edu.ifrn.musicschool;

import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.musicschool.persistencia.modelo.Aluno;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OlaSpringBoot {

    @GetMapping("/")
    public String index(){
        
        return "Olá Spring Boot ";
    }

}

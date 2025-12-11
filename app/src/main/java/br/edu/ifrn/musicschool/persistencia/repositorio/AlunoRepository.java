package br.edu.ifrn.musicschool.persistencia.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.musicschool.persistencia.modelo.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByGmail(String gmail);

}

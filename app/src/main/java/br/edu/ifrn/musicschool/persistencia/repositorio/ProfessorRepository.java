package br.edu.ifrn.musicschool.persistencia.repositorio;

import br.edu.ifrn.musicschool.persistencia.modelo.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    // Buscar professor por email
    Optional<Professor> findByEmail(String email);
    
    // Verificar se email já existe
    boolean existsByEmail(String email);
    
    // Buscar professores por especialidade
    List<Professor> findByEspecialidade(String especialidade);
    
    // Buscar professores por nome (case insensitive)
    List<Professor> findByNomeContainingIgnoreCase(String nome);
}
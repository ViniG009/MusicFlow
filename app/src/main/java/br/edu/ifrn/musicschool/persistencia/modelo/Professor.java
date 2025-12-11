package br.edu.ifrn.musicschool.persistencia.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "Especialidade é obrigatória")
    @Column(nullable = false, length = 50)
    private String especialidade;
    
    @Column(length = 20)
    private String telefone;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal salario;
    
    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;
    
    @Column(name = "experiencia_anos")
    private Integer experienciaAnos;
    
    // 🔗 RELACIONAMENTO 1:N (LADO "1")
    // Um professor pode ter MUITOS alunos
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Aluno> alunos = new ArrayList<>();
    
    // Método para contar alunos
    public int getQuantidadeAlunos() {
        return alunos != null ? alunos.size() : 0;
    }
}
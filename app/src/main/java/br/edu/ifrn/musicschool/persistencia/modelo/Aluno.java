package br.edu.ifrn.musicschool.persistencia.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table ( name = "alunos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Gmail é obrigatório")
    @Column(name = "gmail", nullable = false, unique = true, length = 180)
    private String gmail;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Past(message = "A data de nascimento deve ser anterior à data atual")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "O instrumento é obrigatório")
    @Column(name = "instrumento", nullable = false, length = 50)
    private String instrumento;

    @ManyToOne
    @JoinColumn(name = "professor_id") // Nome da coluna no banco
    private Professor professor;

}

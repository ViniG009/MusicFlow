package br.edu.ifrn.musicschool.web.controladores;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifrn.musicschool.persistencia.modelo.Professor;
import br.edu.ifrn.musicschool.persistencia.repositorio.ProfessorRepository;
import br.edu.ifrn.musicschool.persistencia.modelo.Aluno;
import br.edu.ifrn.musicschool.persistencia.repositorio.AlunoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/alunos")
public class AlunoControler {

    @Autowired
    private AlunoRepository alunoRepository;
    
    // 🔥 ADICIONE ESTA INJEÇÃO DO PROFESSORREPOSITORY
    @Autowired
    private ProfessorRepository professorRepository;

    // LISTAR (JÁ TEM) ✓
    @GetMapping
    public String listar(Model model) {
        List<Aluno> alunos = alunoRepository.findAll();
        model.addAttribute("alunos", alunos);
        return "alunos/lista-alunos";
    }

    // FORMULÁRIO NOVO (CORRIGIDO) ✓
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("aluno", new Aluno());
        
        // 🔥 CORREÇÃO: Use a instância injetada (professorRepository)
        List<Professor> professores = professorRepository.findAll();
        model.addAttribute("professores", professores);
        
        return "alunos/formulario-aluno";
    }

    // SALVAR NOVO (ATUALIZADO) ✓
    @PostMapping
    public String salvar(@Valid @ModelAttribute Aluno aluno, BindingResult result, Model model) {
        // Valida duplicidade
        if (alunoRepository.findByGmail(aluno.getGmail()).isPresent()) {
            result.rejectValue("gmail", "erro.duplicado", "Já existe um aluno com esse Gmail.");
        }

        if (result.hasErrors()) {
            // 🔥 ADICIONE: Recarrega professores se houver erro
            List<Professor> professores = professorRepository.findAll();
            model.addAttribute("professores", professores);
            return "alunos/formulario-aluno";
        }

        alunoRepository.save(aluno);
        return "redirect:/alunos";
    }

    // ------------ AQUI COMEÇA O NOVO ------------
    
    // 1. FORMULÁRIO DE EDIÇÃO (ATUALIZADO)
    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Aluno> alunoOpt = alunoRepository.findById(id);
        
        if (alunoOpt.isEmpty()) {
            return "redirect:/alunos";
        }
        
        model.addAttribute("aluno", alunoOpt.get());
        
        // 🔥 ADICIONE: Carrega a lista de professores
        List<Professor> professores = professorRepository.findAll();
        model.addAttribute("professores", professores);
        
        return "alunos/formulario-editar";
    }
    
    // 2. ATUALIZAR ALUNO (UPDATE - ATUALIZADO)
    @PostMapping("/atualizar/{id}")
public String atualizar(@PathVariable Long id,
                       @ModelAttribute("aluno") Aluno alunoAtualizado,
                       BindingResult result,
                       Model model) {
    
    // 1. Busca o aluno existente
    Optional<Aluno> alunoExistenteOpt = alunoRepository.findById(id);
    if (alunoExistenteOpt.isEmpty()) {
        return "redirect:/alunos";
    }
    
    Aluno alunoExistente = alunoExistenteOpt.get();
    
    // 2. VERIFICA SE A DATA VEIO NULA - SE VEIO, USA A DATA ORIGINAL
    if (alunoAtualizado.getDataNascimento() == null) {
        alunoAtualizado.setDataNascimento(alunoExistente.getDataNascimento());
    }
    
    // 3. Validação de email
    if (!alunoExistente.getGmail().equals(alunoAtualizado.getGmail())) {
        Optional<Aluno> alunoComEmail = alunoRepository.findByGmail(alunoAtualizado.getGmail());
        if (alunoComEmail.isPresent() && !alunoComEmail.get().getId().equals(id)) {
            result.rejectValue("gmail", "erro.duplicado", 
                "Já existe outro aluno com esse Gmail.");
        }
    }
    
    // 4. Se tem erros, mostra formulário com erros
    if (result.hasErrors()) {
        List<Professor> professores = professorRepository.findAll();
        model.addAttribute("professores", professores);
        // Mantém o aluno atualizado no model para mostrar os dados digitados
        model.addAttribute("aluno", alunoAtualizado);
        return "alunos/formulario-editar";
    }
    
    // 5. Atualiza todos os campos
    alunoExistente.setNome(alunoAtualizado.getNome());
    alunoExistente.setGmail(alunoAtualizado.getGmail());
    alunoExistente.setDataNascimento(alunoAtualizado.getDataNascimento());
    alunoExistente.setInstrumento(alunoAtualizado.getInstrumento());
    alunoExistente.setProfessor(alunoAtualizado.getProfessor());
    
    alunoRepository.save(alunoExistente);
    return "redirect:/alunos";
}
    
    // 3. EXCLUIR ALUNO (DELETE) ✓
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        alunoRepository.deleteById(id);
        return "redirect:/alunos";
    }
    
    // 4. DETALHES DO ALUNO - ATUALIZADO ✓
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Optional<Aluno> alunoOpt = alunoRepository.findById(id);
        
        if (alunoOpt.isEmpty()) {
            return "redirect:/alunos";
        }
        
        Aluno aluno = alunoOpt.get();
        
        // Calcula a idade se tiver data de nascimento
        Integer idade = null;
        if (aluno.getDataNascimento() != null) {
            idade = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();
        }
        
        // Adiciona ao modelo
        model.addAttribute("aluno", aluno);
        model.addAttribute("idade", idade);
        
        return "alunos/detalhes-aluno";
    }
}
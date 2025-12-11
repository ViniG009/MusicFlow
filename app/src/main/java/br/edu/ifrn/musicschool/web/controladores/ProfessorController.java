package br.edu.ifrn.musicschool.web.controladores;

import br.edu.ifrn.musicschool.persistencia.modelo.Professor;
import br.edu.ifrn.musicschool.persistencia.repositorio.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    // 📋 LISTAR TODOS OS PROFESSORES
    @GetMapping
    public String listar(Model model) {
        List<Professor> professores = professorRepository.findAll();
        model.addAttribute("professores", professores);
        return "professores/lista-professores";
    }

    // ➕ FORMULÁRIO NOVO PROFESSOR
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("professor", new Professor());
        return "professores/formulario-professor";
    }

    // 💾 SALVAR NOVO PROFESSOR
    @PostMapping
    public String salvar(@Valid @ModelAttribute Professor professor, 
                        BindingResult result, 
                        Model model) {
        
        // Valida duplicidade de email
        if (professorRepository.existsByEmail(professor.getEmail())) {
            result.rejectValue("email", "erro.duplicado", 
                "Já existe um professor com esse email.");
        }

        if (result.hasErrors()) {
            return "professores/formulario-professor";
        }

        professorRepository.save(professor);
        return "redirect:/professores";
    }

    // ✏️ FORMULÁRIO DE EDIÇÃO
    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Professor> professorOpt = professorRepository.findById(id);
        
        if (professorOpt.isEmpty()) {
            return "redirect:/professores";
        }
        
        model.addAttribute("professor", professorOpt.get());
        return "professores/formulario-editar-professor";
    }

    // 🔄 ATUALIZAR PROFESSOR
    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, 
                          @Valid @ModelAttribute Professor professorAtualizado, 
                          BindingResult result, 
                          Model model) {
        
        Optional<Professor> professorExistenteOpt = professorRepository.findById(id);
        if (professorExistenteOpt.isEmpty()) {
            return "redirect:/professores";
        }
        
        Professor professorExistente = professorExistenteOpt.get();
        
        // Valida se mudou o email
        if (!professorExistente.getEmail().equals(professorAtualizado.getEmail())) {
            if (professorRepository.existsByEmail(professorAtualizado.getEmail())) {
                result.rejectValue("email", "erro.duplicado", 
                    "Já existe outro professor com esse email.");
            }
        }
        
        if (result.hasErrors()) {
            return "professores/formulario-editar-professor";
        }
        
        // Atualiza dados
        professorExistente.setNome(professorAtualizado.getNome());
        professorExistente.setEmail(professorAtualizado.getEmail());
        professorExistente.setEspecialidade(professorAtualizado.getEspecialidade());
        professorExistente.setTelefone(professorAtualizado.getTelefone());
        professorExistente.setSalario(professorAtualizado.getSalario());
        professorExistente.setDataContratacao(professorAtualizado.getDataContratacao());
        professorExistente.setExperienciaAnos(professorAtualizado.getExperienciaAnos());
        
        professorRepository.save(professorExistente);
        return "redirect:/professores";
    }

    // 🗑️ EXCLUIR PROFESSOR
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        professorRepository.deleteById(id);
        return "redirect:/professores";
    }

    // 👁️ DETALHES DO PROFESSOR
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Optional<Professor> professorOpt = professorRepository.findById(id);
        
        if (professorOpt.isEmpty()) {
            return "redirect:/professores";
        }
        
        model.addAttribute("professor", professorOpt.get());
        return "professores/detalhes-professor";
    }
}
package br.com.adev.appfinancas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.adev.appfinancas.model.Categoria;
import br.com.adev.appfinancas.model.Transacao;
import br.com.adev.appfinancas.repository.CategoriaRepository;
import br.com.adev.appfinancas.repository.TransacaoRepository;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransacaoRepository tr;

    @RequestMapping("/categorias")
    public ModelAndView listaCategorias(){

        List<Categoria> categorias = categoriaRepository.findAll();
        ModelAndView mv=new ModelAndView("categorias");
        mv.addObject("categorias", categorias);
        return mv;
    }
    @RequestMapping("/categoria/nova")
    public String novaCategoria(){
        return "novaCategoria";
    }
    @RequestMapping(value ="/categoria/nova", method=RequestMethod.POST)
    public String form(Categoria categoria, BindingResult result, RedirectAttributes attributes){
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @RequestMapping(value="/categoria/{id}", method=RequestMethod.GET)
    public ModelAndView detalhesCategoria(@PathVariable("id") long id){
        Categoria categoria = categoriaRepository.findById(id);
        ModelAndView mv = new ModelAndView("/detalhesCategoria");
        mv.addObject("categoria", categoria);

        Iterable<Transacao> transacoes = tr.findByCategoria(categoria);
        mv.addObject("transacoes", transacoes);
        return mv;
    }
 

    @RequestMapping(value="/categoria/{id}", method=RequestMethod.POST)
    public String detalhesCategoriaPost(@PathVariable("id") long id, Transacao transacao, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/categoria/{id}";
        }
        Categoria categoria = categoriaRepository.findById(id);
        transacao.setCategoria(categoria);
        tr.save(transacao);
        //esse trecho deve subtrair do saldo da conta.
        //conta.setSaldo(conta.getSaldo() - transacao.getValor());
        //cr.save(conta);

        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
            
        return "redirect:/categoria/{id}";
    }

    @RequestMapping(value="/editar/categoria/{id}", method=RequestMethod.GET)
    public ModelAndView editarCategoria(@PathVariable("id") long id){
        Categoria categoria = categoriaRepository.findById(id);
        ModelAndView mv = new ModelAndView("/editarCategoria");
        mv.addObject("categoria", categoria);

        Iterable<Transacao> transacoes = tr.findByCategoria(categoria);
        mv.addObject("transacoes", transacoes);
        return mv;
    }
 

    @RequestMapping(value="/editar/categoria/{id}", method=RequestMethod.POST)
    public String editarCategoriaPost(@PathVariable("id") long id, Categoria categoria, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/categoria/{id}";
        }
        Categoria categoriaX = categoriaRepository.findById(id);
        categoriaX.setNome(categoria.getNome());
        categoriaRepository.save(categoriaX);
       
        //esse trecho deve subtrair do saldo da conta.
        //conta.setSaldo(conta.getSaldo() - transacao.getValor());
        //cr.save(conta);

        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
            
        return "redirect:/categorias";
    }

    

    @RequestMapping("/deletarCategoria")
    public String deletarCategoria(long id){
        Categoria categoria = categoriaRepository.findById(id);

        Iterable<Transacao> transa = tr.findByCategoria(categoria);
        //para cada elemento de transa definir categoria como null, 
        for (Transacao transacao : transa) {
            transacao.setCategoria(null);
            tr.save(transacao);
        }
        //depois tr salva elementos de transa
       

        categoriaRepository.delete(categoria);
        return "redirect:/categorias";
    }

}

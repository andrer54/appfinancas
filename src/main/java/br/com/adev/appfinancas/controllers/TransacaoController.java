package br.com.adev.appfinancas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.adev.appfinancas.model.Transacao;
import br.com.adev.appfinancas.repository.TransacaoRepository;

@Controller
public class TransacaoController {

    @Autowired
    private TransacaoRepository tr;

    @GetMapping("/")
    public ModelAndView financas(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Transacao> transacoes = tr.findAll();
        mv.addObject("transacoes", transacoes);
        return mv;
    }

    @GetMapping("/nova")
    public String nova(){
        return "formTransacao";

    }
    @RequestMapping(value="/nova", method=RequestMethod.POST)
    public String form(Transacao transacao){
        
        tr.save(transacao);
        return "redirect:/";
    }
  
    @RequestMapping("/deletarTransacao")
    public String deletarTransacao(long id){
        Transacao transacao = tr.findByIdTransacao(id);
        tr.delete(transacao);
        return "redirect:/";
    }
      //editarTransacao get
    //editarTransacao post
}

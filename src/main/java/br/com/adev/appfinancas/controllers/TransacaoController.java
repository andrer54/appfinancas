package br.com.adev.appfinancas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.adev.appfinancas.model.Categoria;
import br.com.adev.appfinancas.model.Conta;
import br.com.adev.appfinancas.model.TipoDeTransacao;
import br.com.adev.appfinancas.model.Transacao;
import br.com.adev.appfinancas.repository.CategoriaRepository;
import br.com.adev.appfinancas.repository.ContaRepository;
import br.com.adev.appfinancas.repository.TransacaoRepository;

@Controller
public class TransacaoController {

    @Autowired
    private TransacaoRepository tr;
    @Autowired
    private ContaRepository cr;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/")
    public ModelAndView financas(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Transacao> transacoes = tr.findAll();
        mv.addObject("transacoes", transacoes);

        Iterable<Conta> contas = cr.findAll();
        mv.addObject("contas", contas);
        return mv;

    }

    @GetMapping("/transacao/nova")
    public ModelAndView nova(){
        ModelAndView mv = new ModelAndView("formTransacao");
        Iterable<Categoria> categorias = categoriaRepository.findAll();
        mv.addObject("categorias", categorias);
        Iterable<Conta> contas = cr.findAll();
        mv.addObject("contas", contas);
        return mv;

    }
    @RequestMapping(value="/transacao/nova", method=RequestMethod.POST)
    public String form(Transacao transacao, Conta conta, Categoria categoria){
      

        Conta contaX= cr.findByIdConta(conta.getIdConta());
        transacao.setConta(contaX);
        //Quando a transacao é DESPESA
        if(transacao.getTipoDeTransacao()==TipoDeTransacao.DESPESA){
        contaX.setSaldo(contaX.getSaldo() - transacao.getValor());
        cr.save(contaX);
        }else{      //Quando a transacao é RECEITA
            contaX.setSaldo(contaX.getSaldo() + transacao.getValor());
            cr.save(contaX);
        }// else -> tranferencia


        Categoria categoriaX = categoriaRepository.findById(categoria.getId());
        transacao.setCategoria(categoriaX);
        tr.save(transacao);
        return "redirect:/";
    }
  
    @RequestMapping("/deletarTransacao")
    public String deletarTransacao(long id){
        Transacao transacao = tr.findByIdTransacao(id);
        
        if(transacao.getConta()!=null){
        Conta conta = transacao.getConta();
        conta.setSaldo(conta.getSaldo()+transacao.getValor());
        cr.save(conta);
        }

        tr.delete(transacao);
        return "redirect:/";
    }
      //editarTransacao get
      //editarTransacao post
    @RequestMapping("/contato")
    public String contato(){
        return "contato";
    }
}

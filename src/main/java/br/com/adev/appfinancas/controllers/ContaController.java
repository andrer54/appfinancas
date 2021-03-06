package br.com.adev.appfinancas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.adev.appfinancas.model.Conta;
import br.com.adev.appfinancas.model.TipoDeTransacao;
import br.com.adev.appfinancas.model.Transacao;
import br.com.adev.appfinancas.repository.ContaRepository;
import br.com.adev.appfinancas.repository.TransacaoRepository;

@Controller
public class ContaController {

    @Autowired
    private ContaRepository cr;

    @Autowired
    private TransacaoRepository tr;

    @RequestMapping(value ="/conta/nova", method=RequestMethod.GET)
    public String form(){
        return "formConta";
    }

    @RequestMapping(value ="/conta/nova", method=RequestMethod.POST)
    public String form(Conta conta, BindingResult result, RedirectAttributes attributes){
        cr.save(conta);
        return "redirect:/contas";
    }

    @RequestMapping("/contas")
    public ModelAndView listaContas(){
        ModelAndView mv = new ModelAndView("contas");
        Iterable<Conta> contas = cr.findAll();
        mv.addObject("contas", contas);
        return mv;
    }
    @RequestMapping(value="/{idConta}", method=RequestMethod.GET)
    public ModelAndView detalhesConta(@PathVariable("idConta") long idConta){
        Conta conta = cr.findByIdConta(idConta);
        ModelAndView mv = new ModelAndView("/detalhesConta");
        mv.addObject("conta", conta);

        Iterable<Transacao> transacoes = tr.findByConta(conta);
        mv.addObject("transacoes", transacoes);
        return mv;
    }
 

    @RequestMapping(value="/{idConta}", method=RequestMethod.POST)
    public String detalhesContaPost(@PathVariable("idConta") long idConta, Transacao transacao, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{idConta}";
        }
        Conta conta= cr.findByIdConta(idConta);
        transacao.setConta(conta);
        tr.save(transacao);
        //esse trecho deve subtrair do saldo da conta.
        conta.setSaldo(conta.getSaldo() - transacao.getValor());
        cr.save(conta);

        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
            
        return "redirect:/{idConta}";
    }
    @RequestMapping("/contas/transferencia")
    public ModelAndView transferencia(){
        ModelAndView mv = new ModelAndView("transferencia");
        Iterable<Conta> contas = cr.findAll();
        mv.addObject("contas", contas);
        return mv;
    }
    @RequestMapping(value="/contas/transferencia", method=RequestMethod.POST)
    public String transferencia(long idContaOrigem, long idContaDestino, Double valor, RedirectAttributes attributes){

        Conta contaOrigem= cr.findByIdConta(idContaOrigem);
        Conta contaDestino= cr.findByIdConta(idContaDestino);
        
        contaOrigem.setSaldo(contaOrigem.getSaldo()-valor);
        contaDestino.setSaldo(contaDestino.getSaldo()+valor);

        cr.save(contaOrigem);
        cr.save(contaDestino);

        Transacao transacao = new Transacao();
        transacao.setDescricao("Transferencia para conta: "+contaDestino.getNomeConta());
        transacao.setValor(valor);
        transacao.setConta(contaOrigem);
   //     transacao.setCategoria(categoria);
        transacao.setTipoDeTransacao(TipoDeTransacao.TRANSFERENCIA);
        tr.save(transacao);

        
        return "redirect:/";
    }
    @RequestMapping(value="/editar/{idConta}", method=RequestMethod.GET)
    public ModelAndView editarConta(@PathVariable("idConta") long idConta){
        Conta conta = cr.findByIdConta(idConta);
        ModelAndView mv = new ModelAndView("/formEditConta");
        mv.addObject("conta", conta);
        return mv;
    }
 

    @RequestMapping(value="/editar/{idConta}", method=RequestMethod.POST)
    public String editarConta(@PathVariable("idConta") long idConta, Transacao transacao, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{idConta}";
        }
        Conta conta= cr.findByIdConta(idConta);
        transacao.setConta(conta);
        tr.save(transacao);
        //esse trecho deve subtrair do saldo da conta.
        conta.setSaldo(conta.getSaldo() - transacao.getValor());
        cr.save(conta);

        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
            
        return "redirect:/{idConta}";
    }

        @RequestMapping("/deletarConta")
    public String deletarConta(long id){
        Conta conta = cr.findByIdConta(id);

        Iterable<Transacao> transa = tr.findByConta(conta);
        //para cada elemento de transa definir categoria como null, 
        for (Transacao transacao : transa) {
            transacao.setConta(null);
            tr.save(transacao);
        }
        //depois tr salva elementos de transa
       

        cr.delete(conta);
        return "redirect:/contas";
    
    }

}

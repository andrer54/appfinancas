package br.com.adev.appfinancas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransacaoController {

    @GetMapping("/")
    public String financas(){
        return "index";
    }

    @GetMapping("/nova")
    public String nova(){
        return "formTransacao";
    }
}

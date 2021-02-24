package br.com.adev.appfinancas.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.adev.appfinancas.model.Categoria;
import br.com.adev.appfinancas.repository.CategoriaRepository;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

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
        return "redirect:/";
    }
}

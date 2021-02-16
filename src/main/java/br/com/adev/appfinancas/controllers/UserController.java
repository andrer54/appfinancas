package br.com.adev.appfinancas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/")
    public String helloword(){
        return "hello word! FINANCE APP";
    }
    
}

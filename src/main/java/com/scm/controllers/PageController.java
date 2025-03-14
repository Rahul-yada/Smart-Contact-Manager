package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {


    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("welcome to home page");
        model.addAttribute("name","RahulTech Development");
        model.addAttribute("YoutubeChannel", "https://www.youtube.com/");
        model.addAttribute("github_Repo", "Rahul_yada");

        return "home";
    }

    @RequestMapping("/about")
    public String AboutPage(Model model){
        model.addAttribute("isLogin", true);
        System.out.println("This is about page of my project");
        return "about";
    }

    @RequestMapping("/service")
    public String ServicePage(){
        System.out.println("this is service page of my project");
        return "service";
    }



}

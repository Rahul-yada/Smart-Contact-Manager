package com.scm.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.scm.Forms.UserForm;
import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {


    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    


    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("name","RahulTech Development");
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @RequestMapping("/about")
    public String AboutPage( Model model){
        model.addAttribute("currentPage", "about");
        System.out.println("This is about page of my project");

        return "about";
    }

    @RequestMapping("/service")
    public String ServicePage(Model model){
        model.addAttribute("currentPage", "service");
        System.out.println("this is service page of my project");
        return "service";
    }

    @GetMapping("/contact")
    public String contactPage(Model model){
        model.addAttribute("currentPage", "contact");
        System.out.println("this is contact page of my project");
        return "contact";
    }

    
    @GetMapping("/login")
    public String loginPage(){
        System.out.println("this is login page of my project");
        return "login";
    }

    
    @GetMapping("/register")
      public String registerPage(Model model){
       UserForm userForm = new UserForm();
    //    yaha se direct default data bhej sakte hai form me.....
        //   userForm.setName("Rahul");
        //   userForm.setEmail("rahul@gmail.com");
        //   userForm.setPassword("12345678");
        //   userForm.setPhoneNumber("9930458585");
        //   userForm.setAbout("Write something about your self");
       model.addAttribute("userForm", userForm); 
        return "register";
    } 
      
        //  this is register form handler code 
    @RequestMapping(value ="/do-register", method=RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){ 
        System.out.println("Processing registeration successfully");
        // some steps are perform before redirect ...............
        // fetch form data
        System.out.println(userForm);
        // validate the form data--- we use @valid for verification of data in above form --after that use BindingResult that hold the result of verification if it has error then it return the register form again..
        if(rBindingResult.hasErrors()){
            return "register";
        }


        


        // save into database 
        // userServices
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic("https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg");
        User savedUser = userService.saveUser(user);

        System.out.println("user save successfully");
        // add some message for confirmation 
        Message message = Message.builder().content("Registration Successfull").type(MessageType.green).build();
        session.setAttribute("message",message);
      

        return "redirect:/register";
    }


}

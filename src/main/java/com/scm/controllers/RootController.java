package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {


    private Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

 // this method provide all informaion about user name and email who logged from any way 

    @ModelAttribute
    public void addLoggedInUserInformation(Model model , Authentication authentication){
    if(authentication == null){
        return;
    }
    String username = Helper.getEmailOfLoggedUser(authentication);
    logger.info("User logged in : {}", username);
    

     // database se data ko fetch kar sakte hai      
     User user = userService.getUserByEmail(username);
     System.out.println(user.getName());
     System.out.println(user.getEmail());
     model.addAttribute("loggedInUser", user);

    }
}

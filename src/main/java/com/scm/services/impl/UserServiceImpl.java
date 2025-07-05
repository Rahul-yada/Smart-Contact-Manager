package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.helpers.*;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service

public class UserServiceImpl implements UserService {

   
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EmailService emailService;

    @Override
    public User saveUser(User user) {
        // using this method to create IDs 
      String userId = UUID.randomUUID().toString();
      user.setUserid(userId);
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      
       user.setRoleList(List.of(AppConstants.ROLE_USER));
       logger.info(user.getProviders().toString());

      

    //   here create token before store data into database 

      String emailToken = UUID.randomUUID().toString();
    //   first store the token 
      user.setEmailToken(emailToken);
    //   then save the user 
      User savedUser =  userRepo.save(user);
      String emailLink  = Helper.getLinkForEmailVerfication(emailToken);

      emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);
      

      return savedUser;


    }

    @Override
    public Optional<User> getUserById(String id) {

       return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user2 = userRepo.findById(user.getUserid()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        // user2 ko upadate karnge 
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderUserId(user.getProviderUserId());

        // save the user into database 
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);

        
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        userRepo.delete(user2);

      
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 !=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
       User user = userRepo.findByEmail(email).orElse(null);
       return user !=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
       return UserRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
       return userRepo.findByEmail(email).orElse(null);
    }

 
     
}

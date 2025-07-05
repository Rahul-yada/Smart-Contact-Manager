package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
// import org.hibernate.Session;
import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import com.scm.Forms.ContactForm;
import com.scm.Forms.ContactSearchForm;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @SuppressWarnings("FieldMayBeFinal")
    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    // add contact page 
    public String addContactView( Model model){
        ContactForm contactForm = new ContactForm();

        // this way we can filled the form from backend 
        // contactForm.setName("Rahul yadav");
        // contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result , Authentication authentication,HttpSession session){
        

      String username = Helper.getEmailOfLoggedUser(authentication);

        // process the data  

        // 1.validate 
    if(result.hasErrors()){
        session.setAttribute("message", Message.builder()
        .content("Please correct the following errors")
        .type(MessageType.red)
        .build());
        return "user/add_contact";
    }

        // form -> contact 
           User user = userService.getUserByEmail(username);
    //    2. process the contact 
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contactService.save(contact);    
        System.out.println(contactForm);

       

        //  3. set the message to be displayed the view 
        session.setAttribute("message",
        Message.builder()
        .content("You have successfully added a new contact")
        .type(MessageType.green)
        .build());
        

        return "redirect:/user/contacts/add";

    }

    @RequestMapping
public String viewContact(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
    @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
    @RequestParam(value = "direction", defaultValue = "asc") String direction,
    Model model, Authentication authentication) {

       
        // load all the user contacts 
        String username = Helper.getEmailOfLoggedUser(authentication);

        User user = userService.getUserByEmail(username);

     Page<Contact> pageContact =  contactService.getByUser(user, page, size,sortBy, direction);


     model.addAttribute("pageContact", pageContact );
     model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
     model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "/user/contacts";
    }



    // search handler of contact page 
@RequestMapping("/search")
public String searchHandler(
   @ModelAttribute ContactSearchForm contactSearchForm,
    @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE + "") int size,
    @RequestParam(value = "page",defaultValue = "0") int page,
    @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
    @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model, Authentication authentication
    ){


        
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedUser(authentication));
        Page<Contact> pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }




        // logger.info("pageContact {}", pageContact );
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", contactSearchForm);

        
    return "user/search";
}


// delete contact 

     @RequestMapping("/delete/{contactId}")
     public String deleteContact(
        @PathVariable("contactId") String contactId,
        HttpSession session
        ){

        contactService.delete(contactId);
       session.setAttribute("message", 
       Message.builder().content("Contact is Delete successfully !!")
       .type(MessageType.green).build()
       );
        logger.info("contactId {} deleted", contactId);
        return "redirect:/user/contacts";
     }

    @GetMapping("/view/{contactId}")
     public String updateContactFormView(
        @PathVariable("contactId") String contactId,
        Model model){

            var contact = contactService.getById(contactId);

            ContactForm contactForm = new ContactForm();
            contactForm.setName(contact.getName());
            contactForm.setEmail(contact.getEmail());
            contactForm.setPhoneNumber(contact.getPhoneNumber());
            contactForm.setAddress(contact.getAddress());
            contactForm.setDescription(contact.getDescription());
            contactForm.setFavorite(contact.isFavorite());
            contactForm.setWebsiteLink(contact.getWebsiteLink());
            contactForm.setLinkedInLink(contact.getLinkedInLink());

            model.addAttribute("contactForm", contactForm);
            model.addAttribute("contactId", contactId);



        return "user/update_contact_view";
     }

     @RequestMapping(value="/update/{contactId}", method=RequestMethod.POST)
public String updateContact(
    @PathVariable("contactId") String contactId, 
    @Valid @ModelAttribute ContactForm contactForm,
    Model model) {


    Contact contact = contactService.getById(contactId);
    
   
    contact.setName(contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setPhoneNumber(contactForm.getPhoneNumber());
    contact.setAddress(contactForm.getAddress());
    contact.setDescription(contactForm.getDescription());
    contact.setFavorite(contactForm.isFavorite());
    contact.setWebsiteLink(contactForm.getWebsiteLink());
    contact.setLinkedInLink(contactForm.getLinkedInLink());

    // Update the contact in the database
    contactService.update(contact); 

    logger.info("Updated contact: {}", contact);

    // model.addAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());

 
    return "redirect:/user/contacts/view/" + contactId;
}


}

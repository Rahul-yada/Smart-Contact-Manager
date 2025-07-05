package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private  ContactRepo contactRepo;
    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            contact.setId(UUID.randomUUID().toString()); 
        }
        return contactRepo.save(contact);
    }
     @Override
     public Contact update(Contact contact) {
         // Ensure the ID is not null before proceeding
         if (contact.getId() == null) {
             throw new IllegalArgumentException("Contact ID cannot be null");
         }
     
         Contact existingContact = contactRepo.findById(contact.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contact.getId()));
         
         existingContact.setName(contact.getName());
         existingContact.setEmail(contact.getEmail());
         existingContact.setPhoneNumber(contact.getPhoneNumber());
         existingContact.setAddress(contact.getAddress());
         existingContact.setDescription(contact.getDescription());
         existingContact.setPicture(contact.getPicture());
         existingContact.setFavorite(contact.isFavorite());
         existingContact.setWebsiteLink(contact.getWebsiteLink());
         existingContact.setLinkedInLink(contact.getLinkedInLink());
         
         return contactRepo.save(existingContact);
     }

    @Override
    public List<Contact> getAll() {
         return contactRepo.findAll();

         }

    @Override
    public Contact getById(String id) {

        return contactRepo.findById(id).
        orElseThrow( ()-> new ResourceNotFoundException("Contact not found"+id));
           }

    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id).
        orElseThrow( ()-> new ResourceNotFoundException("Contact not found"+id));
    
        contactRepo.delete(contact);
         }



    @Override
    public List<Contact> getByUserId(String userId) {
     return contactRepo.findByUserId(userId);

        }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);
        return contactRepo.findByUser(user,pageable);
        }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,User user) {
        
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);
       return contactRepo.findByUserAndNameContaining( user,nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page,size,sort);
       return contactRepo.findByUserAndEmailContaining( user,emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order,User user) {
                Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
                var pageable = PageRequest.of(page,size,sort);
               return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);
    }

  

}

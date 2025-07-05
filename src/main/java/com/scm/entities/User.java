package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Table(name="users")  
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
@Builder

public class User  implements UserDetails{


    @Id
    private String Userid;
    @Column(name = "user_name",nullable =  false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String about;
    @SuppressWarnings("unused")
    private String phoneNumber;
    private String profilePic;
    @Column(length = 1000)
    private boolean enabled = true;
    private boolean phoneVerified = false;
    private boolean emailVerified = false;

    @Enumerated(value = EnumType.STRING)
    private Providers providers = Providers.SELF;
    private String providerUserId;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();


    // user verification done after sending email to him 
     
    private String emailToken;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // this is list of roles example : [user,admin] 
        // we make the list of role collection in SimpleGrantedAuthority[admin,user] 

       Collection<SimpleGrantedAuthority> roles =   roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }
    @Override
    public String getUsername() {
        return this.email;
         }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public String getPassword(){
        return this.password;
    }


    
   




}
  
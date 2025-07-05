package com.scm.Forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class UserForm {
     
    @NotBlank(message = "Name is required")
    @Size(min = 3,message = "Min 3 characters is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6,message = "Min 6 characters is required")
    private String  password;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    private String  email;
    
    @NotBlank(message = "PhoneNumber is required")
    @Size(min = 8,max = 12,message = "Invalid Number")
    private String  phoneNumber;

    @NotBlank(message = "About is required")
    private String  about;




    

}

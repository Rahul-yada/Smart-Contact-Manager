package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedUser(Authentication authentication) {

        // Check if user logged in via OAuth2 (e.g., Google)
        if (authentication instanceof OAuth2AuthenticationToken oAuth2Token) {


            String clientId = oAuth2Token.getAuthorizedClientRegistrationId();
            OAuth2User oauth2User = oAuth2Token.getPrincipal();

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting email from Google");
                return oauth2User.getAttribute("email");
            }

        }

        // Fallback for normal login (username-password based)
        System.out.println("Getting information from database");
        return authentication.getName(); 
    }

    public static String getEmailOfLoggedInUser(Authentication authentication) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmailOfLoggedInUser'");
    }


    public static String getLinkForEmailVerfication(String emailToken){

        String link = "http://localhost:8080/auth/verify-email?token=" + emailToken;

        return link;
    }

    


}

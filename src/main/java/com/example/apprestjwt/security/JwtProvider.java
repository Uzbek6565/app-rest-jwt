package com.example.apprestjwt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    String secretKey = "Maxfiy_soz";
    long expireTimeInMillis = 36000000;//36.000.000 milliseconds = 10 hours

    public String generateToken(String username){
        Date expireDate = new Date(System.currentTimeMillis() + expireTimeInMillis);

        String token = Jwts
                .builder()
                .setSubject(username)       //Subject is a unique field of user such as, username, phone number, email etc.
                .setIssuedAt(new Date())    //When is created the token
                .setExpiration(expireDate)  //When does the token expires
                .signWith(SignatureAlgorithm.HS512, secretKey)  //create token using HS512 algorithm and secret key
                .compact();//compact() creates the token

        return token;
    }

    public boolean validateToken(String token){
        try{
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        String username = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }

}

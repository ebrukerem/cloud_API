package com.urlshortener.urlshortener.controllers;
import com.urlshortener.urlshortener.models.Users;
import com.urlshortener.urlshortener.repositories.URLRepository;
import com.urlshortener.urlshortener.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @RequestMapping (value = "/registration", method = RequestMethod.POST)
    public void registration(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setDate(LocalDate.now());
        user.setRole("user");
        userRepository.save(user);
    }

    @RequestMapping (value = "/lastMonthsUserAnalytics", method = RequestMethod.GET)
    public int[] userAnalytics(@RequestBody Users user) {
        int[] userA = new int[12];
        if(userRepository.findByMail(user.getEmail()).get(0).getRole().equals("admin"))
        {
            for(int i = 0;i <userA.length; i++)
            {
                List<Users> u = userRepository.findByDateBetween(LocalDate.now().minusMonths(i+1),LocalDate.now().minusMonths(i));
                userA[i]=  u.size();
            }
            return userA;

        }
        else{
            System.out.println(user.getRole());
            logger.warn("You dont have authorization");
            return null;
        }

    }




}

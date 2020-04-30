package com.urlshortener.urlshortener.controllers;
import com.urlshortener.urlshortener.models.Urls;
import com.urlshortener.urlshortener.models.Users;
import com.urlshortener.urlshortener.repositories.URLRepository;
import com.urlshortener.urlshortener.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/Urls")
public class URLController {
    @Autowired
    private URLRepository repository;
    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(URLRepository.class);
    @RequestMapping(value = "/shorten", method = RequestMethod.POST)
    public Urls createURL(@Valid @RequestBody Urls url) {

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );

        if (urlValidator.isValid(url.getURL())) {

            if(repository.findByUrl(url.getURL()).isEmpty())
            {
                String hash = Hashing.murmur3_32().hashString(url.getURL(), StandardCharsets.UTF_8).toString();

                byte[] salt = new byte[0];
                try {
                    salt = getSalt();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                hash = get_SHA_1_SecurePassword(hash, salt);
                hash = hash.substring(1,6);
                System.out.println("URL Id generated: "+ hash);
                Urls urlNew= new Urls(ObjectId.get(),url.getURL(),hash,url.getUserMail(), url.getdate(),0);
                urlNew.set_id(ObjectId.get());
                repository.save(urlNew);
                System.out.println(urlNew);
                return urlNew;
            }
            else{
                if(repository.findByURLAndUsermail(url.getUserMail(),url.getURL()).isEmpty())
                {
                        String hash = Hashing.murmur3_32().hashString(url.getURL(), StandardCharsets.UTF_8).toString();

                        byte[] salt = new byte[0];
                        try {
                            salt = getSalt();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        hash = get_SHA_1_SecurePassword(hash, salt);
                        hash = hash.substring(1, 6);
                        System.out.println("URL Id generated: " + hash);
                        Urls urlNew = new Urls(ObjectId.get(), url.getURL(), hash, url.getUserMail(),  url.getdate(), 0);
                        urlNew.set_id(ObjectId.get());
                        repository.save(urlNew);
                        System.out.println(urlNew);
                        return urlNew;


                }

                else{

                    logger.warn("This url has been created", repository.findByUrl(url.getURL()).get(0).getHash());
                    return repository.findByUrl(url.getURL()).get(0);

                }
            }


        }

        throw new RuntimeException("URL Invalid: " + url);
    }


    @RequestMapping(value = "/customshorten", method = RequestMethod.POST)
    public Urls createCustomURL(@Valid @RequestBody Urls url,@RequestHeader String hash ) {
            if(repository.findByUrl(url.getURL()).isEmpty() )
            {
                Urls urlNew= new Urls(ObjectId.get(),url.getURL(),hash,url.getUserMail(), url.getdate(),0);
                urlNew.set_id(ObjectId.get());
                repository.save(urlNew);
                System.out.println(urlNew);
                return urlNew;
            }
            else{
                if(repository.findByURLAndUsermail(url.getUserMail(),url.getURL()).isEmpty())
                {
                        Urls urlNew = new Urls(ObjectId.get(), url.getURL(), hash, url.getUserMail(), url.getdate(), 0);
                        urlNew.set_id(ObjectId.get());
                        repository.save(urlNew);
                        System.out.println(urlNew);
                        return urlNew;

                }

                else{
                    System.out.println(url.getUserMail());
                    logger.warn("This url has been created", repository.findByUrl(url.getURL()).get(0).getHash());
                    return repository.findByUrl(url.getURL()).get(0);
                }



            }



    }

    @RequestMapping (value = "/lastMonthUrlCreationAnalytics", method = RequestMethod.GET)
    public int urlAnalytics(@RequestBody Users user) {
        if(userRepository.findByMail(user.getEmail()).get(0).getRole().equals("admin"))
        {
            List<Urls> u = repository.findByDateBetween(LocalDate.now().minusMonths(1),LocalDate.now());
            return u.size();
        }
        else{
            logger.warn("You dont have authorization");
            return -1;
        }

    }

    @RequestMapping (value = "/lastMonthRedirectionAnalytics", method = RequestMethod.GET)
    public int redirectionAnalytics(@RequestBody Users user) {
        if(userRepository.findByMail(user.getEmail()).get(0).getRole().equals("admin"))
        {
            List<Urls> u = repository.findBycreationDateBetween(LocalDate.now().minusMonths(1),LocalDate.now());
            int count = 0;
            for(int i= 0; i < u.size(); i++)
            {
                count+= u.get(i).getNoOfClick();
            }
            return count;
        }
        else{
            logger.warn("You dont have authorization");
            return -1;
        }

    }

    @GetMapping(value = "/{hash}")
    public void getUrl(@PathVariable String hash, HttpServletResponse httpServletResponse) {
        if(repository.findByHashing(hash).isEmpty())
        {
            throw new RuntimeException("There is no  URL for : " + hash);
        }
        else{
            Urls url = repository.findByHashing(hash).get(0);
            url.setNoOfClick(url.getNoOfClick() + 1);
            repository.save(url);
            System.out.println("URL Retrieved: " + url.getURL() + "noOfClick" + url.getNoOfClick());
            httpServletResponse.setHeader("Location", url.getURL());
            httpServletResponse.setStatus(302);

        }

    }



    @RequestMapping(value = "/mostpopularOverall", method = RequestMethod.GET)
    public List<Urls> mostViewedUrls() {
        List<Urls> url = null;
        url = repository.findTop10ByOrderByNoOfClickDesc();


        return url;
    }

    @RequestMapping(value = "/userURL", method = RequestMethod.GET)
    public List<Urls> userURL(@RequestHeader String usermail) {
        List<Urls> url = null;
        url = repository.findByUsermail(usermail);


        return url;
    }

    @RequestMapping(value = "/mostpopularUser", method = RequestMethod.GET)
    public List<Urls> mostViewedUserUrls(@RequestHeader String usermail) {
        List<Urls> url= null;
       System.out.println();
        url = repository.findByOrderByNoOfClickDesc(usermail);
        return url;
    }


 @RequestMapping(value = "/noOfClick", method = RequestMethod.GET)
 public int getUrlClick(@RequestBody String url) {
     if(repository.findByHashing(url).isEmpty())
     {
         throw new RuntimeException("There is shortened URL for " + url);
     }
     return repository.findByHashing(url).get(0).getNoOfClick();
 }


    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String get_SHA_1_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }


}


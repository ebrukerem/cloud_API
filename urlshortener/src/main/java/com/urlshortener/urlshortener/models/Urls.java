package com.urlshortener.urlshortener.models;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.access.annotation.Secured;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Document
public class Urls {
    @Id
    public ObjectId _id;
    public String URL;
    public String hash;
    @Field
    @Indexed(name="date_expire", expireAfterSeconds=0)
    public LocalDate date;

    public LocalDate creationDate;
    public String userMail;
    public int noOfClick;
    public Urls(){}

    public Urls(ObjectId _id, String URL, String hash,String userMail, LocalDate date, int noOfClick){
       this._id = _id;
       this.URL = URL;
       this.hash = hash;
       this.date = date;
       this.userMail = userMail;
       this.noOfClick = noOfClick;
       this.creationDate = LocalDate.now();
    }


    public String getUserMail() { return userMail; }
    public void setUserMail(String userMail) { this.userMail = userMail; }

    public String get_id() { return _id.toHexString(); }
    public void set_id(ObjectId _id) { this._id = _id; }

    public String getURL() { return URL; }
    public void setURL(String URL) { this.URL = URL; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public LocalDate getdate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getNoOfClick() { return noOfClick; }
    public void setNoOfClick(int noOfClick) { this.noOfClick = noOfClick; }

    public LocalDate getCreationDate() { return creationDate; }

}

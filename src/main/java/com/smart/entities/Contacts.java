package com.smart.entities;


import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Entity
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String name;
    private String nickName;
    private String work;
    private String email;
    private String phone;
    private String profilePic;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @ManyToOne()
    private User user;

    // constructor
    public Contacts() {
        super();
        // TODO Auto-generated constructor stub
    }

    // using field constructor
    public Contacts(String name, String nickName, String work, String email, String phone, String profilePic, String description, User user) {
        this.name = name;
        this.nickName = nickName;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.profilePic = profilePic;
        this.description = description;
        this.user = user;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        return this.cId == ((Contacts) o).getcId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser());
    }
}

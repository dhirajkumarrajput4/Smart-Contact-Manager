package com.smart.service;


import com.smart.entities.Contacts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ContactService {

    public Page<Contacts> findContactsByUser(Integer userId, Pageable pageable);

    public Optional<Contacts> findById(Integer id);

    public void delete(Contacts contact);
    public void save(Contacts contacts);

}

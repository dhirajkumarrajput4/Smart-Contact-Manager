package com.smart.service;

import com.smart.dao.ContactRepository;
import com.smart.entities.Contacts;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Page<Contacts> findContactsByUser(Integer userId, Pageable pageable) {
        return this.contactRepository.findContactsByUser(userId, pageable);
    }

    @Override
    public Optional<Contacts> findById(Integer id) {
        return this.contactRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Contacts contact) {
        this.contactRepository.delete(contact);
    }

    @Override
    public void save(Contacts contacts) {
        this.contactRepository.save(contacts);
    }
}

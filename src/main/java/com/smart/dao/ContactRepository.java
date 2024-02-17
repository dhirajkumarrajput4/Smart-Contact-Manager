package com.smart.dao;

import com.smart.entities.Contacts;
import com.smart.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contacts,Integer> {

    @Query("from Contacts as c where c.user.id =:userId")
    public Page<Contacts> findContactsByUser(@Param("userId") int userId, Pageable pageable);
    public Optional<Contacts> findBycId(int cId);
    
    public List<Contacts> findByNameContainingAndUser(String name,User string);

}

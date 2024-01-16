package com.smart.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionHelper {
    @Autowired
    private ObjectFactory<HttpSession> sessionFactory;

    public HttpSession getSession() {
        return sessionFactory.getObject();
    }

    public void removeSessionAttribute(String key){
        try {
            HttpSession session = getSession();
            session.removeAttribute(key);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

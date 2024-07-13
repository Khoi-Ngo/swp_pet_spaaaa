package org.swp.Email;


import org.springframework.messaging.MessagingException;

public interface EmailSender {
    String send( String email) throws MessagingException, jakarta.mail.MessagingException;
}

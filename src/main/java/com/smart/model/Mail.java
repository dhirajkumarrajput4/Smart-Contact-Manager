package com.smart.model;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail
{
    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private List <Object> attachments;

    public Date getMailSendDate() {
        return new Date();
    }
}
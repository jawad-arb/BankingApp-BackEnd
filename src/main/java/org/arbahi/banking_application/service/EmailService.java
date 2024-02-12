package org.arbahi.banking_application.service;

import org.arbahi.banking_application.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}

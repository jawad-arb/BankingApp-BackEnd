package org.arbahi.banking_application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Banking Application",
                description = "BackEnd REST APIs Application For Banking App",
                version = "v1.0",
                contact = @Contact(
                        url = "https://github.com/jawad-arb/BankingApp-BackEnd-"
                )


        )

)
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}

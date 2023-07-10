package com.motus.fileoperations.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fileOperationsOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("ezgiefe@gmail.com");
        contact.setName("Ezgi Efe");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8000");
        localServer.setDescription("Server URL in Local environment");

        Server productionServer = new Server();
        productionServer.setUrl("http://localhost:8000");
        productionServer.setDescription("Server URL in Production environment");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("File Operations")
                .contact(contact)
                .description("Reading files from a folder, saving their data and retrieving from database")
                .version("1.0")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer,productionServer));

    }
}

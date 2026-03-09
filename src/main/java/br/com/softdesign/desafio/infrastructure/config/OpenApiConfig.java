package br.com.softdesign.desafio.infrastructure.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Desafio SoftDesign API")
                .version("1.0.0")
                .description("API Referente ao desafio técnico")
                .contact(contact())
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    private Contact contact() {
        return new Contact()
                .name("Alan Oliveira")
                .email("alanjhone@gmail.com")
                .url("https://github.com/alanjholiveira");
    }
}
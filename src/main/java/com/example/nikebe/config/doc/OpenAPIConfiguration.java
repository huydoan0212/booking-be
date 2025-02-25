package com.example.nikebe.config.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(" SKHCN App Learning API - documentation ")
                        .version("v3.0")
                        .description("Api for developer about skhcn.com")
                        .contact(contact())
                        .termsOfService("Please dont't share this link for anyone if no required"))
                .components(
                        new Components().addSecuritySchemes("Authorization", securityScheme())
                )
                .servers(server());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").name("Authorization").description("Paste token in here: ");
    }

    private Contact contact() {
        return new Contact().name("SKHCN App Learning").email("applearning@gmail.com ").url("https://skhcn-applearning.com/");
    }

    private List<Server> server() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().description("Dev backend ").url("http://localhost:8080/api/"));
//        servers.add(new Server().description("Dev frontend").url("https://gateway.dev.meu-solutions.com/booking-v2/api/v1.0"));
        return servers;
    }

    @Bean
    public OperationCustomizer globalResponseOperationCustomizer() {
        return (operation, handlerMethod) -> {
            ApiResponse apiResponse = operation.getResponses().get("200");
            if (apiResponse != null && apiResponse.getContent() != null) {
                MediaType mediaType = apiResponse.getContent().get("application/json");
                if (mediaType != null) {
                    // Lấy schema gốc (được sinh ra từ chữ ký controller, ví dụ là FormEntity)
                    Schema<?> originalSchema = mediaType.getSchema();

                    // Tạo schema wrapper cho GlobalResponse
                    Schema wrapperSchema = new Schema<>().type("object");
                    // Các thuộc tính của GlobalResponse
                    wrapperSchema.addProperties("message", new Schema<>().type("string").example("string"));
                    // Chỗ này, responseData sẽ chứa schema của entity gốc
                    wrapperSchema.addProperties("responseData", originalSchema);
                    wrapperSchema.addProperties("success", new Schema<>().type("boolean").example(true));
                    wrapperSchema.addProperties("status", new Schema<>().type("integer").example(200));
                    wrapperSchema.addProperties("violations", new Schema<>().type("object")
                            .example("{ \"fieldName\": \"string\", \"message\": \"string\" }"));
                    wrapperSchema.addProperties("path", new Schema<>().type("string").example("string"));
                    wrapperSchema.addProperties("timestamp", new Schema<>().type("integer").example(0));

                    // Gán schema mới này vào response
                    mediaType.setSchema(wrapperSchema);
                }
            }
            return operation;
        };
    }
}



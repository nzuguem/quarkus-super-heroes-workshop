package io.quarkus.workshop.superheroes.hero;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@ApplicationPath("/")
@OpenAPIDefinition(
   info = @Info(
       title = "Hero API",
       description = "This API allows CRUD operations on a hero",
       version = "1.0",
       contact = @Contact(
           name = "Kevin NZUGUEM"
       )
   ),
    servers = @Server(
        url = "http://locahost:8083"
    ),
    externalDocs = @ExternalDocumentation(
        url = "https://quarkus.io/blog/quarkus-superheroes-to-the-rescue/",
        description = "All the quarkus workshop"
    )
)
public class HeroApplication extends Application {
}

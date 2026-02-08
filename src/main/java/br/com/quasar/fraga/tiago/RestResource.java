package br.com.quasar.fraga.tiago;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// Exemplo de bean CDI para injeção
@ApplicationScoped
class ExampleBean {
    public String getMessage() {
        return "JEE Vivo (com CDI)";
    }
}

@Path("/rest")
public class RestResource {
    @Inject
    private ExampleBean exampleBean;  // Demonstra CDI funcionando

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage() {
        if (exampleBean == null) {
            return "CDI NÃO injetou o bean! (null)";
        }
        return exampleBean.getMessage();
    }
}
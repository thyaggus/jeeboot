package br.com.quasar.fraga.tiago.resource;

import br.com.quasar.fraga.tiago.entidade.Pessoa;
import br.com.quasar.fraga.tiago.service.PessoaService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

@RequestScoped
@Path("/pessoa")
public class PessoaResource {

    @Inject
    private PessoaService service;

    @GET
    @Path("/ping")
    @Produces("application/json;carset=utf-8")
    public String ping() {
        return "Estou Vivo";
    }

    @GET
    @Path("/listar")
    @Produces("application/json;carset=utf-8")
    public List<Pessoa> listrPessoas() {
        return service.listarPessoas();
    }

    @POST
    @Path("/novo")
    @Consumes("application/json;carset=utf-8")
    public void adicionarPessoa(Pessoa pessoa) {
        service.adicionarPessoa(pessoa);
    }

    @POST
    @Path("/remover")
    @Consumes("application/json;carset=utf-8")
    public void removerPessoa(Pessoa pessoa) {
        service.removerPessoa(pessoa);
    }
}

package br.com.quasar.fraga.tiago.service;

import br.com.quasar.fraga.tiago.entidade.Pessoa;
import jakarta.enterprise.context.RequestScoped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class PessoaService {

    private List<Pessoa> pessoas;

    public List<Pessoa> listarPessoas() {
        return Collections.unmodifiableList(pessoas);
    }

    public void adicionarPessoa(Pessoa pessoa) {
        if (this.pessoas == null) {
            this.pessoas = new ArrayList<>();
        }
        this.pessoas.add(pessoa);
    }

    public void removerPessoa(Pessoa pessoa) {
        this.pessoas.remove(pessoa);
    }

}

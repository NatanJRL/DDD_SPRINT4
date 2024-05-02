package br.com.fiap.model.cliente;

import br.com.fiap.model.endereco.Endereco;
import br.com.fiap.model.Usuario;
import br.com.fiap.model.empresa.Empresa;
import br.com.fiap.model.telefone.Telefone;
import br.com.fiap.produto.ClienteJaPossuiProdutoException;
import br.com.fiap.produto.Produto;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private List<Produto> produtos = new ArrayList<>();
    private String funcao;
    private boolean ativo;
    private List<Telefone> telefones = new ArrayList<>();
    private Empresa empresa;


    //Construtor para criação pelo terminal
    public Cliente(String email, String senha, String nomeCompleto, String funcao, String dataNascimento, Endereco endereco){
        super(email, senha, nomeCompleto, dataNascimento, endereco);
        this.funcao = funcao;
        this.ativo = true;
    }

    //Construtor para resposta do banco
    public Cliente(Long id, String email, String senha, String nomeCompleto, String funcao, String dataRegistro, String dataNascimento, boolean ativo, Endereco endereco){
        super(id, email, senha, nomeCompleto, dataRegistro, dataNascimento, endereco);
        this.funcao = funcao;
        this.ativo = ativo;

    }
    public Cliente(DadosInsercaoClienteDTO dadosInsercaoClienteDTO){
        super(
                dadosInsercaoClienteDTO.email(),
                dadosInsercaoClienteDTO.senha(),
                dadosInsercaoClienteDTO.nomeCompleto(),
                dadosInsercaoClienteDTO.dataNascimento(),
                new Endereco(dadosInsercaoClienteDTO.endereco()));
        this.funcao = dadosInsercaoClienteDTO.funcao();
        this.ativo = true;
    }


    public void adicionaTelefone(Telefone telefone){
        if (telefone == null){
            throw new IllegalArgumentException("Telefone inválido");
        }
        for(Telefone telefoneAtual : this.telefones ){
            if(telefone.getNumero() == telefoneAtual.getNumero()){
                throw new IllegalArgumentException("Telefone já existe");
            }
        }
        this.telefones.add(telefone);
    }

    public void adicionaProduto(Produto produto){
        if (produto == null){
            throw new IllegalArgumentException("Produto inválido");
        }
        for(Produto produtoAtual : this.produtos){
            if(produto.getNome().equals(produtoAtual.getNome())){
                throw new ClienteJaPossuiProdutoException("Produto já adicionado na lista");
            }
        }
        this.produtos.add(produto);
    }
    public void inativar(){
        this.ativo = false;
    }

    @Override
    public String toString() {
        return super.toString() + "\n " + " Função: '" + this.funcao + '\'' + "\n " +
                " Status: '" + (this.ativo? "Ativo'": "Inativo'");
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public String getFuncao() {
        return funcao;
    }

    public boolean isAtivo() {
        return ativo;
    }
}

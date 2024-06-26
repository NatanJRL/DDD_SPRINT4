package br.com.fiap.model.cliente;

import br.com.fiap.model.endereco.Endereco;
import br.com.fiap.model.usuario.Usuario;
import br.com.fiap.model.empresa.Empresa;
import br.com.fiap.model.telefone.Telefone;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private String funcao;
    private boolean ativo;
    private List<Telefone> telefones = new ArrayList<>();
    private Empresa empresa;


    //Construtor para criação através do insert into
    public Cliente(String email, String senha, String nomeCompleto,Telefone telefone, Empresa empresa,String funcao, String dataNascimento, Endereco endereco){
        super(email, senha, nomeCompleto, dataNascimento, endereco);
        this.funcao = funcao;
        this.ativo = true;
        this.telefones.add(telefone);
        this.empresa = empresa;
    }

    //Construtor para resposta do banco
    public Cliente(
            Long id,
            String email,
            String senha,
            String nomeCompleto,
            String funcao,
            String dataRegistro,
            String dataNascimento,
            boolean ativo,
            Endereco endereco,
            Empresa empresa,
            Telefone telefone
    ){
        super(id, email, senha, nomeCompleto, dataRegistro, dataNascimento, endereco);
        this.funcao = funcao;
        this.ativo = ativo;
        this.empresa = empresa;
        this.telefones.add(telefone);
    }
    //construtor para envio serializado
    public Cliente(DadosInsercaoClienteDTO dadosInsercaoClienteDTO){
        super(
                dadosInsercaoClienteDTO.email(),
                dadosInsercaoClienteDTO.senha(),
                dadosInsercaoClienteDTO.nomeCompleto(),
                dadosInsercaoClienteDTO.dataNascimento(),
                new Endereco(dadosInsercaoClienteDTO.endereco()));

        this.telefones.add(new Telefone(dadosInsercaoClienteDTO.telefone()));
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

    public List<Telefone>getTelefones() {
        return this.telefones;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}

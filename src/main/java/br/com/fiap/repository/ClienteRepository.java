package br.com.fiap.repository;

import br.com.fiap.model.cliente.Cliente;
import br.com.fiap.model.empresa.Empresa;
import br.com.fiap.model.empresa.TamanhoEmpresa;
import br.com.fiap.model.endereco.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteRepository{

    private static final Map<String, String> TABLE_COLUMNS = Map.of(
            "ID","id_cliente",
            "ID_DE_USUARIO", "id_usuario",
            "STATUS", "st_cliente",
            "FUNCAO", "nm_funcao"
    );

    UsuarioRepository usuarioRepository;

    public ClienteRepository(){
        this.usuarioRepository = new UsuarioRepository();
    }

    public Long inserirCliente(Cliente cliente) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "insert into t_sf_cliente(%s, %s, %s) values(?, ?, ?)"
                             .formatted(
                                     TABLE_COLUMNS.get("ID_DE_USUARIO"),
                                     TABLE_COLUMNS.get("FUNCAO"),
                                     TABLE_COLUMNS.get("STATUS")))
        ) {

            Long idUsuario = usuarioRepository.inserirUsuario(cliente);
            preparedStatement.setLong(1, idUsuario);
            preparedStatement.setString(2, cliente.getFuncao());
            preparedStatement.setBoolean(3, cliente.isAtivo());
            preparedStatement.executeUpdate();

            return idUsuario;

        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Cliente getClientById(Long id){
        try (Connection connection = DBConnection.getConnection();
             Statement comandoSelect = connection.createStatement()){

            ResultSet resultSet =
                    comandoSelect.executeQuery(
                                    """
                            select t_sf_usuario.*, t_sf_cliente.*, t_sf_endereco.%s, t_sf_endereco.%s, t_sf_empresa.*
                            from
                            t_sf_usuario
                            join
                            t_sf_cliente on t_sf_usuario.%s = t_sf_cliente.%s
                            join
                            t_sf_endereco on t_sf_usuario.%s = t_sf_endereco.%s
                            join t_sf_empresa on t_sf_cliente.%s = t_sf_empresa.%s where t_sf_usuario.%s = %s
                            order by t_sf_usuario.%s
                            """.formatted(
                                            EnderecoRepository.TABLE_COLUMNS.get("ESTADO"),
                                            EnderecoRepository.TABLE_COLUMNS.get("PAIS"),
                                            UsuarioRepository.TABLE_COLUMNS.get("ID"),
                                            TABLE_COLUMNS.get("ID_DE_USUARIO"),
                                            UsuarioRepository.TABLE_COLUMNS.get("ID"),
                                            EnderecoRepository.TABLE_COLUMNS.get("ID_DE_USUARIO"),
                                            ClienteRepository.TABLE_COLUMNS.get("ID"),
                                            EmpresaRepository.TABLE_COLUMNS.get("ID_DO_CLIENTE"),
                                            UsuarioRepository.TABLE_COLUMNS.get("ID"),
                                            id,
                                            UsuarioRepository.TABLE_COLUMNS.get ("ID")
                                    ));

            if (resultSet.next()){
                return createClienteFromResultSet(resultSet);
            }

        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        try (
                Connection connection = DBConnection.getConnection();
                Statement comandoSelect = connection.createStatement()
        ){
            List<Cliente> retorno = new ArrayList<>();
            ResultSet resultSet =
                    comandoSelect.executeQuery("""
                            select t_sf_usuario.*, t_sf_cliente.*, t_sf_endereco.%s, t_sf_endereco.%s, t_sf_empresa.*
                            from
                            t_sf_usuario
                            join
                            t_sf_cliente on t_sf_usuario.%s = t_sf_cliente.%s
                            join
                            t_sf_endereco on t_sf_usuario.%s = t_sf_endereco.%s
                            join t_sf_empresa on t_sf_cliente.%s = t_sf_empresa.%s
                            """
                            .formatted(
                                    EnderecoRepository.TABLE_COLUMNS.get("ESTADO"),
                                    EnderecoRepository.TABLE_COLUMNS.get("PAIS"),
                                    UsuarioRepository.TABLE_COLUMNS.get("ID"),
                                    TABLE_COLUMNS.get("ID_DE_USUARIO"),
                                    UsuarioRepository.TABLE_COLUMNS.get("ID"),
                                    EnderecoRepository.TABLE_COLUMNS.get("ID_DE_USUARIO"),
                                    TABLE_COLUMNS.get("ID"),
                                    EmpresaRepository.TABLE_COLUMNS.get("ID_DO_CLIENTE")
                                    ));

            while(resultSet.next()){
                retorno.add(createClienteFromResultSet(resultSet));
            }
            if (retorno.isEmpty()) return null;

            return retorno;
        }

        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    private Cliente createClienteFromResultSet(ResultSet resultSet) throws SQLException{
        return new Cliente(
                resultSet.getLong(TABLE_COLUMNS.get("ID_DE_USUARIO")),
                resultSet.getString(UsuarioRepository.TABLE_COLUMNS.get("EMAIL")),
                resultSet.getString(UsuarioRepository.TABLE_COLUMNS.get("SENHA")),
                resultSet.getString(UsuarioRepository.TABLE_COLUMNS.get("NOME_COMPLETO")),
                resultSet.getString(TABLE_COLUMNS.get("FUNCAO")),
                resultSet.getDate(UsuarioRepository.TABLE_COLUMNS.get("DATA_DE_REGISTRO")).toString(),
                resultSet.getDate(UsuarioRepository.TABLE_COLUMNS.get("DATA_DE_NASCIMENTO")).toString(),
                resultSet.getBoolean(TABLE_COLUMNS.get("STATUS")),
                new Endereco(
                        resultSet.getString(EnderecoRepository.TABLE_COLUMNS.get("ESTADO")),
                        resultSet.getString(EnderecoRepository.TABLE_COLUMNS.get("PAIS"))),
                new Empresa(
                        resultSet.getString(EmpresaRepository.TABLE_COLUMNS.get("NOME_DA_EMPRESA")),
                        TamanhoEmpresa.valueOf(resultSet.getString(
                                EmpresaRepository.TABLE_COLUMNS.get("TAMANHO_DA_EMPRESA"))))
        );
    }
}


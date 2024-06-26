package br.com.fiap;


import br.com.fiap.model.cliente.DadosAtualizacaoClienteDTO;
import br.com.fiap.model.cliente.DadosInsercaoClienteDTO;
import br.com.fiap.model.cliente.DadosLoginClienteDTO;
import br.com.fiap.model.cliente.ListagemClienteDTO;
import br.com.fiap.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/clientes")
public class ClienteResource {


    private ClienteService clienteService = new ClienteService();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ListagemClienteDTO> listarClientes(){
        return clienteService.listarTodosClientes();
    }
    @GET
    @Path(("{id}"))
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<ListagemClienteDTO> listarClientePorId(@PathParam("id") Long id){
        return Optional.ofNullable(clienteService.recuperarPorID(id));
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inserirCliente(@Valid DadosInsercaoClienteDTO insercaoClienteDTO){
            clienteService.inserir(insercaoClienteDTO);
            return Response.status(Response.Status.CREATED).build();

    }

    @POST
    @Path("login")
    public Response login(DadosLoginClienteDTO dadosLogin){
        boolean autorizado = clienteService.login(dadosLogin);
        if (!autorizado){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarCliente(DadosAtualizacaoClienteDTO dadosAtualizacao){
        clienteService.atualizar(dadosAtualizacao);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{id}")
    public Response deletarCliente(@PathParam("id") Long id){
        clienteService.deletar(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}



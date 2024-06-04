package com.redhat.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.ClientPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.UserPermissionEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CustomRealmClientResource {
    private final KeycloakSession session;
    private final RealmModel realm;
    private final AdminPermissionEvaluator evaluator;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GET
    @Path("clients/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listClientsByName(@PathParam("name") String name) {
        logger.info("Received request to list clients containing the name {}", name);

        logger.info("Verify if logged user has permission to list clients");
        ClientPermissionEvaluator clientPermissionEvaluator = evaluator.clients();
        clientPermissionEvaluator.requireList();

        logger.info("Executing query for clients");
        Stream<ClientModel> clients = session.clients().getClientsStream(realm);
        List<Map<String, String>> clientModels = clients
                .filter(clientModel -> clientModel.getName().toLowerCase().contains(name))
                .map(clientModel -> Map.of("clientId",clientModel.getClientId(),"name",clientModel.getName(),"publicClient",String.valueOf(clientModel.isPublicClient()),"protocol",clientModel.getProtocol()))
                .toList();
        logger.info("Query returns {} clients", clientModels.size());

        return Response.ok(clientModels).build();

    }
}

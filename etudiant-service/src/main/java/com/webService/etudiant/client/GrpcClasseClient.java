package com.webService.etudiant.client;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webService.grpc.config.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class GrpcClasseClient {
    
    private final ClasseServiceGrpc.ClasseServiceBlockingStub stub;

    public GrpcClasseClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8012)
                .usePlaintext()
                .build();

        stub = ClasseServiceGrpc.newBlockingStub(channel);
    }

    public ClasseResponse ajouterClasse(String name, String description, String cycle, String filiere) {
        ClasseRequest request = ClasseRequest.newBuilder()
                .setName(name)
                .setDescription(description)
                .setCycle(cycle)
                .setFiliere(filiere)
                .build();

        return stub.ajouter(request);
    }

    public ClasseResponse rechercherClasseParId(String id) {
        ClasseIdRequest request = ClasseIdRequest.newBuilder()
                .setId(id)
                .build();

        return stub.rechercher(request);
    }

    public List<ClasseResponse> lister() {
        Empty request = Empty.newBuilder().build();
        ClasseListResponse response = stub.lister(request);
        return response.getClassesList();
    }
}

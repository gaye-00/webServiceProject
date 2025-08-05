package com.webService.classe.controller;

import com.webService.classe.service.ClasseService;
import com.webService.grpc.config.*;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ClasseController extends ClasseServiceGrpc.ClasseServiceImplBase {

    private final ClasseService classeService;

    @Override
    public void ajouter(ClasseRequest request, StreamObserver<ClasseResponse> responseObserver) {
        ClasseResponse response = classeService.create(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void lister(Empty request, StreamObserver<ClasseListResponse> responseObserver) {
        List<ClasseResponse> list = classeService.getAll();

        ClasseListResponse.Builder builder = ClasseListResponse.newBuilder();
        builder.addAllClasses(list);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void rechercher(ClasseIdRequest request, StreamObserver<ClasseResponse> responseObserver) {
        classeService.getById(request.getId())
                .ifPresentOrElse(
                    response -> {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    },
                    () -> responseObserver.onError(new Exception("Classe non trouvée"))
                );
    }

    // public void getById(String id, StreamObserver<ClasseResponse> responseObserver) {
    //     Optional<ClasseResponse> response = classeService.getById(id);

    //     response.ifPresentOrElse(
    //         resp -> {
    //             responseObserver.onNext(resp);
    //             responseObserver.onCompleted();
    //         },
    //         () -> responseObserver.onError(new Exception("Classe non trouvée"))
    //     );
    // }

    @Override
    public void modifier(ClasseUpdateRequest request, StreamObserver<ClasseResponse> responseObserver) {
        ClasseRequest updateRequest = ClasseRequest.newBuilder()
                .setName(request.getName())
                .setDescription(request.getDescription())
                .setCycle(request.getCycle())
                .setFiliere(request.getFiliere())
                .build();

        classeService.update(request.getId(), updateRequest)
                .ifPresentOrElse(
                    response -> {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    },
                    () -> responseObserver.onError(new Exception("Classe à modifier non trouvée"))
                );
    }

    @Override
    public void supprimer(ClasseIdRequest request, StreamObserver<DeleteResponse> responseObserver) {
        boolean success = classeService.delete(request.getId());

        DeleteResponse response = DeleteResponse.newBuilder()
                .setSuccess(success)
                .setMessage(success ? "Classe supprimée avec succès" : "Erreur lors de la suppression")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

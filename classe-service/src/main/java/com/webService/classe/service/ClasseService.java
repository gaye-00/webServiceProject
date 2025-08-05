package com.webService.classe.service;

import com.webService.classe.model.CYCLE;
import com.webService.classe.model.Classe;
import com.webService.classe.model.FILIERE;
import com.webService.classe.repository.ClasseRepository;
import com.webService.grpc.config.ClasseRequest;
import com.webService.grpc.config.ClasseResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClasseService {

    private final ClasseRepository classeRepository;
    private final Logger logger = LoggerFactory.getLogger(ClasseService.class);

    public ClasseResponse create(ClasseRequest request) {
        Classe classe = mapToEntity(request);
        classeRepository.save(classe);
        logger.info("Classe créée avec succès");
        return mapToGrpcResponse(classe);
    }

    public List<ClasseResponse> getAll() {
        return classeRepository.findAll()
                .stream()
                .map(this::mapToGrpcResponse)
                .collect(Collectors.toList());
    }

    public Optional<ClasseResponse> getById(String id) {
        return classeRepository.findById(id).map(this::mapToGrpcResponse);
    }

    public Optional<ClasseResponse> update(String id, ClasseRequest request) {
        Optional<Classe> optionalClasse = classeRepository.findById(id);
        if (optionalClasse.isPresent()) {
            Classe classe = optionalClasse.get();
            classe.setName(request.getName());
            classe.setDescription(request.getDescription());
            classe.setCycle(CYCLE.valueOf(request.getCycle()));
            classe.setFiliere(FILIERE.valueOf(request.getFiliere()));
            classe.setLastModifiedAt(LocalDateTime.now());
            classeRepository.save(classe);
            return Optional.of(mapToGrpcResponse(classe));
        }
        return Optional.empty();
    }

    public boolean delete(String id) {
        try {
            classeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression", e);
            return false;
        }
    }

    private Classe mapToEntity(ClasseRequest request) {
        return Classe.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cycle(CYCLE.valueOf(request.getCycle()))
                .filiere(FILIERE.valueOf(request.getFiliere()))
                .creatAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }

    private ClasseResponse mapToGrpcResponse(Classe classe) {
        return ClasseResponse.newBuilder()
                .setId(classe.getId())
                .setName(classe.getName())
                .setDescription(classe.getDescription())
                // .setCycle(com.webService.grpc.config.Cycle.valueOf(classe.getCycle()))
                .setCycle(classe.getCycle().name())
                .setFiliere(classe.getFiliere().name())
                .build();
    }
}

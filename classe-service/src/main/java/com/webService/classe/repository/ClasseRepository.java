package com.webService.classe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.webService.classe.model.Classe;

@Repository
public interface ClasseRepository extends MongoRepository<Classe, String> {

}
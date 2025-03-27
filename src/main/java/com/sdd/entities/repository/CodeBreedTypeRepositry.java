package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CodeBreedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeBreedTypeRepositry extends JpaRepository<CodeBreedType,Integer> {


    List<CodeBreedType> findByCatIdOrderByDescrAsc(String cat_id);
    CodeBreedType findByBreedId(String cat_id);
}


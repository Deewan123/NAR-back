package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CodeFromCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeFromCategoryRepositry extends JpaRepository<CodeFromCategory,Integer> {


    CodeFromCategory findByCatIdOrderByDescrAsc(String catId);
    List<CodeFromCategory> findAllByOrderByDescrAsc();
}


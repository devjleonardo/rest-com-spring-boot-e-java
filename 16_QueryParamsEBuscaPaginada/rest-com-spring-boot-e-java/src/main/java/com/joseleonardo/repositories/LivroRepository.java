package com.joseleonardo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joseleonardo.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

}

package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swp.entity.Pet;

@Repository
public interface IPetrepository extends JpaRepository<Pet, Integer> {
}

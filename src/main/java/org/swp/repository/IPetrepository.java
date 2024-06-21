package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.Pet;
import org.swp.entity.Service;

import java.util.List;

@Repository
public interface IPetrepository extends JpaRepository<Pet, Integer> {
    @Query(value = "SELECT * FROM tbl_pet WHERE user_id = (SELECT id FROM tbl_user WHERE username = :username) AND is_delete = FALSE", nativeQuery = true)
    List<Pet> findByUserName(@Param("username") String username);
}

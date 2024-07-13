package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.Pet;
import org.swp.entity.Service;

import java.util.List;

@Repository
public interface IPetrepository extends JpaRepository<Pet, Integer> {
    @Query(value = "SELECT * FROM tbl_pet WHERE user_id = (SELECT id FROM tbl_user WHERE username = :username) AND is_deleted = FALSE", nativeQuery = true)
    List<Pet> findByUserName(@Param("username") String username);

    @Query(value = "select * from tbl_pet where is_deleted = false ", nativeQuery = true)
    List<Pet> findAllPet();

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_pet SET is_deleted = true WHERE user_id = :id", nativeQuery = true)
    void deletedAllPetByUserId(@Param("id") int id);

}

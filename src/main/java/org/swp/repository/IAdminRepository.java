package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.swp.entity.User;
import java.util.List;

import java.util.Collection;

@Repository
public interface IAdminRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.role = 1 AND u.isDeleted = FALSE")
    Collection<Object> findAllCustomerACC();

    @Query(value = "SELECT * FROM tbl_user WHERE role = 2 AND is_deleted = FALSE", nativeQuery = true)
    List<User> findAllShopOwnerAcc();

}

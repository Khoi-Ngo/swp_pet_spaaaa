package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.Service;
import org.swp.enums.TypePet;

import java.util.Collection;
import java.util.List;

@Repository
public interface IServiceRepository extends JpaRepository<Service, Integer> {
    @Query(value = "SELECT * FROM tbl_service WHERE is_deleted = FALSE ORDER BY created_time DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Service> findLatestServices(@Param("numberOfRecords") int numberOfRecords);

    @Query(value = "SELECT * FROM tbl_service WHERE is_deleted = FALSE ORDER BY nomination DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Service> findMostRcmdServices(@Param("numberOfRecords") int numberOfRecords);


    @Query(value = "SELECT * FROM tbl_service  WHERE type_pet = :typePet ORDER BY nomination DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Service> findMostRcmdServices(@Param("typePet") TypePet typePet, @Param("numberOfRecords") int numberOfRecords);

    @Query(value = "SELECT * FROM tbl_service  WHERE shop_id = :shopId and is_deleted = false ORDER BY nomination DESC ", nativeQuery = true)
    List<Service> findAllByShopId(@Param("shopId") int shopId);

    @Query(value = "SELECT * FROM tbl_service WHERE is_deleted = FALSE", nativeQuery = true)
    List<Service> findAllService();
}

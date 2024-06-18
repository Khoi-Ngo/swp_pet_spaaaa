package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swp.entity.other.Nomination;

public interface INominationRepository extends JpaRepository<Nomination, Integer> {
}

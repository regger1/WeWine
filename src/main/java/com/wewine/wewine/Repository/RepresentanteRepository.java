package com.wewine.wewine.Repository;

import com.wewine.wewine.Entity.RepresentanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepresentanteRepository extends JpaRepository<RepresentanteEntity, Long> {
    Optional<RepresentanteEntity> findByEmail(String email);
}

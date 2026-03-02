package com.wewine.wewine.Repository;

import com.wewine.wewine.Entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    List<ClienteEntity> findByRepresentanteId(Long representanteId);

    Long countByRepresentanteId(Long representanteId);
}

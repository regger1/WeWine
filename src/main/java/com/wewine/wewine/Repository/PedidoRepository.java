package com.wewine.wewine.Repository;

import com.wewine.wewine.Entity.PedidoEntity;
import com.wewine.wewine.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByRepresentanteId(Long representanteId);

    List<PedidoEntity> findByStatusIn(List<StatusPedido> statuses);

    List<PedidoEntity> findByStatusNotIn(List<StatusPedido> statuses);

    List<PedidoEntity> findByDataBetweenAndStatusIn(LocalDate startDate, LocalDate endDate, List<StatusPedido> statuses);

    @Query("SELECT p FROM PedidoEntity p ORDER BY p.codigoPedido DESC")
    List<PedidoEntity> findTopNRecentOrders();

    @Query("SELECT p FROM PedidoEntity p WHERE p.status IN :statuses AND p.data BETWEEN :startDate AND :endDate")
    List<PedidoEntity> findByStatusInAndDateBetween(@Param("statuses") List<StatusPedido> statuses,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);
}

package com.cg.repository;

import com.cg.domain.dto.order.IOrderDTO;
import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.order.OrderResDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByTableAndStatus(AppTable appTable, EBillStatus eBillStatus);

    List<Order> findAllByTableAndStatus(AppTable appTable, EBillStatus eBillStatus);


    @Query("SELECT NEW com.cg.domain.dto.order.OrderResDTO (" +
                "od.id, " +
                "od.totalAmount, " +
                "od.table, " +
                "od.createdAt, " +
                "od.createdBy" +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false "
    )
    Page<OrderResDTO> getAllOrderDTOByDeletedIsFalse(Pageable pageable);


    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
                "od.id, " +
                "od.totalAmount, " +
                "od.status, " +
                "od.table, " +
                "od.createdAt " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND od.status = :orderStatus"
    )
    List<OrderDTO> getOrderDTOByStatus(@Param("orderStatus") EBillStatus orderStatus);


    @Query(value = "SELECT * FROM vw_get_order_by_status_cooking", nativeQuery = true)
    List<IOrderDTO> getOrderDTOByStatusCooking();


    List<Order> getOrderById(Long id);

    @Query("SELECT NEW com.cg.domain.dto.order.OrderResDTO (" +
                "od.id, " +
                "od.totalAmount, " +
                "od.table, " +
                "od.createdAt, " +
                "od.createdBy" +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND DATE_FORMAT(od.createdAt,'%Y-%m-%d') > :startDay " +
            "AND DATE_FORMAT(od.createdAt,'%Y-%m-%d') < :endDay "
    )
    Page<OrderResDTO> getAllOrderDTOByDayToDay(@Param("startDay") String startDay, @Param("endDay") String endDay, Pageable pageable);


    @Modifying
    @Query("UPDATE Order AS od SET od.deleted = true WHERE od.id = :orderId")
    void softDelete(@Param("orderId") long orderId);
}

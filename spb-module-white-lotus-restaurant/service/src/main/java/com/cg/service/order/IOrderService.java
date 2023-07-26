package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderItem.IOrderItemKitchenTableDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface IOrderService extends IGeneralService<Order, Long> {

    Page<OrderResDTO> getAllOrderDTOByDeletedIsFalse(Pageable pageable);

    Page<OrderResDTO> getAllOrderDTOByDayToDay(String startDay, String endDay, Pageable pageable);

    List<OrderDTO> getOrderDTOByStatus(EBillStatus orderStatus);

    List<IOrderDTO> getOrderDTOByStatusCooking();

    List<Order> getOrderById(Long id);

    int countProductInOrder(List<OrderItemKitchenTableDTO> orderItemList);

    int countProductInOrderItem(List<IOrderItemKitchenTableDTO> orderItemList);

    Optional<Order> findByTableAndStatus(AppTable appTable, EBillStatus eBillStatus);

    List<Order> findAllByTableAndStatus(AppTable appTable, EBillStatus eBillStatus);

    List<OrderKitchenTableDTO> getAllOrderKitchenCookingByTable(EOrderItemStatus status);

    OrderAndListTableDTO createWithOrderItems(OrderWithOrderItemReqDTO orderWithOrderItemReqDTO, AppTable appTable);

    OrderAndListTableDTO updateWithOrderItems(OrderWithOrderItemReqDTO orderWithOrderItemReqDTO, Order order);

    BigDecimal calculateTotalAmount(Long orderId);

    void softDelete(long orderId);
}

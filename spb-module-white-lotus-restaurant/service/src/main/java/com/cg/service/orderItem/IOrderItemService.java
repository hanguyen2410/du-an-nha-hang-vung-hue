package com.cg.service.orderItem;

import com.cg.domain.dto.orderItem.*;
import com.cg.domain.dto.product.IProductReportDTO;
import com.cg.domain.dto.report.ProductReportDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.dto.orderItem.OrderItemBillResDTO;
import com.cg.domain.dto.orderItem.OrderItemCurrentDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface IOrderItemService extends IGeneralService<OrderItem, Long> {
    List<OrderItemResDTO> getOrderItemResDTOByOrderId(Long orderId);

    List<OrderItemCurrentDTO> getOrderItemResDTOByOrderIdAndOrderItemStatus(Long orderId, EOrderItemStatus orderItemStatus);

    List<OrderItem> findAllByOrderId(Long orderId);

    List<OrderItem> findAllByOrderAndStatus(Order order, EOrderItemStatus status);

    List<OrderItemDTO> getOrderItemDTOByOrderId(Long orderId);

    List<OrderItemBillHistoryDTO> getOrderItemDTOSumByOrderId(Long orderId);

    List<OrderItem> getAllByOrder(Order order);

    List<OrderItemBillResDTO> getOrderItemBillResDTOByOrderId(Long orderId);

    Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusWaiter(Long orderId, Long productId, String note);

    Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusDelivery(Long orderId, Long productId, String note);

    Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusStockOut(Long orderId, Long productId, String note);

    Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note);

    List<OrderItemKitchenGroupDTO> getOrderItemByStatusGroupByProduct(EOrderItemStatus status);

    List<IOrderItemKitchenGroupDTO> getOrderItemByStatusCookingGroupByProduct();

    List<OrderItemKitchenWaiterDTO> getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus status);

    List<IOrderItemKitchenWaiterDTO> getOrderItemByStatusWaiterGroupByTableAndProduct();

    List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(EOrderItemStatus orderItemStatus, Long tableId);

    List<IOrderItemKitchenTableDTO> getOrderItemByStatusCookingAndTable(@Param("tableId") Long tableId);

    OrderItemKitchenWaiterDTO changeStatusFromCookingToWaiterOfProduct(OrderItem orderItemCooking);

    void changeStatusFromCookingToWaiterToProductOfOrder(OrderItem orderItemCooking);

    void changeStatusFromCookingToWaiterAllProductOfTable(Order order);

    OrderItemKitchenWaiterDTO changeStatusFromCookingToStockOutOfProduct(OrderItem orderItemCooking);

    void changeStatusFromCookingToStockOutToProduct(OrderItem orderItemCooking);

    OrderItemKitchenWaiterDTO changeStatusFromCookingToWaiterToOneProductOfGroup(Long productId, String note);

    List<OrderItemKitchenWaiterDTO> changeStatusFromCookingToWaiterToAllProductsOfGroup(Long productId, String note);

    void changeStatusFromWaiterToDeliveryOfProduct(OrderItem orderItemWaiter);

    void changeStatusFromWaiterToDeliveryToProductOfOrder(OrderItem orderItemWaiter);

    void changeStatusFromWaiterToDeliveryAllProductOfTable(Order order);

    void changeStatusFromWaiterToStockOutOfProduct(OrderItem orderItemWaiter);

    void changeStatusFromWaiterToStockOutToProductOfOrder(OrderItem orderItemWaiter);

    void changeStatusFromDeliveryToDoneOfProduct(OrderItem orderItemDelivery);

    void changeStatusFromDeliveryToDoneToProductOfOrder(OrderItem orderItemDelivery);

    void changeStatusFromDeliveryToDoneToTableAll(Order order);

    void deleteOrderItemStockOut(OrderItem orderItem);

    List<ProductReportDTO> getTop5ProductBestSell(int month, int year, Pageable pageable);

    List<ProductReportDTO> getTop5ProductUnmarketable(int month, int year, Pageable pageable);

    List<IProductReportDTO> getTop5BestSellCurrentMonth();

    List<IProductReportDTO> getTop5ProductUnMarketTableCurrentMonth();

}

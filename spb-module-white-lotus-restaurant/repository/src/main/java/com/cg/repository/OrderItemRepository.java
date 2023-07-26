package com.cg.repository;

import com.cg.domain.dto.bill.BillPrintItemDTO;
import com.cg.domain.dto.orderItem.*;
import com.cg.domain.dto.product.IProductReportDTO;
import com.cg.domain.dto.orderItem.OrderItemBillResDTO;
import com.cg.domain.dto.orderItem.OrderItemCurrentDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.dto.report.ProductReportDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.enums.EOrderItemStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.status, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.table, " +
                "oi.product, " +
                "oi.order " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId "
    )
    List<OrderItemDTO> getOrderItemDTOByOrderId(@Param("orderId") Long orderId);

    List<OrderItem> getAllByOrder(Order order);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemBillResDTO (" +
                "oi.product.title, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.amount " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId "
    )
    List<OrderItemBillResDTO> getOrderItemBillResDTOByOrderId(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.bill.BillPrintItemDTO (" +
                "oi.product.title, " +
                "SUM(oi.quantity), " +
                "oi.unit, " +
                "oi.price, " +
                "SUM(oi.amount) " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "GROUP BY oi.product.title, oi.unit, oi.price"
    )
    List<BillPrintItemDTO> getAllBillPrintItemDTOByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemBillHistoryDTO (" +
                "oi.id, " +
                "oi.product.title, " +
                "SUM(oi.quantity), " +
                "oi.price, " +
                "SUM(oi.amount) " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "GROUP BY oi.product.title, oi.unit, oi.price"
    )
    List<OrderItemBillHistoryDTO> getOrderItemDTOSumByOrderId(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemResDTO (" +
                "oi.id, " +
                "oi.unit, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.status, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.product.id, " +
                "oi.productTitle, " +
                "oi.product.productAvatar " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId "
    )
    List<OrderItemResDTO> getOrderItemResDTOByOrderId(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemCurrentDTO (" +
                "oi.id, " +
                "oi.unit, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.status, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.product " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "AND oi.status = :orderItemStatus"
    )
    List<OrderItemCurrentDTO> getOrderItemResponseDTOByOrderIdAndOrderItemStatus(@Param("orderId") Long orderId, @Param("orderItemStatus") EOrderItemStatus orderItemStatus);


    List<OrderItem> findAllByOrderAndStatus(Order order, EOrderItemStatus status);

    List<OrderItem> findAllByProductIdAndNoteAndStatusOrderByIdAsc(Long productId, String note, EOrderItemStatus eOrderItemStatus);

    List<OrderItem> findAllByProductIdAndNoteAndStatus(Long productId, String note, EOrderItemStatus eOrderItemStatus);


    Optional<OrderItem> findByOrderAndProductAndStatusAndNote(Order order, Product product, EOrderItemStatus status, String note);


    @Query("SELECT oi " +
            "FROM OrderItem AS oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.product.id = :productId " +
            "AND oi.note = :note " +
            "AND oi.status = :orderItemStatus"
    )
    Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndOrderItemStatus(@Param("orderId") Long orderId, @Param("productId") Long productId, @Param("note") String note, @Param("orderItemStatus") EOrderItemStatus orderItemStatus);


    List<OrderItem> findAllByOrderId(Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenGroupDTO (" +
                "pd.id, " +
                "pd.title, " +
                "oi.note, " +
                "SUM(oi.quantity), " +
                "oi.unit " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "WHERE oi.status = :orderItemStatus " +
            "GROUP BY oi.product.id, oi.note "
    )
    List<OrderItemKitchenGroupDTO> getOrderItemByStatusGroupByProduct(@Param("orderItemStatus") EOrderItemStatus orderItemStatus);

    @Query(value = "SELECT * FROM vw_get_order_item_by_status_cooking_group_by_product", nativeQuery = true)
    List<IOrderItemKitchenGroupDTO> getOrderItemByStatusCookingGroupByProduct();


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenWaiterDTO (" +
                "oi.id, " +
                "oi.table.id, " +
                "oi.table.name, " +
                "pd.id, " +
                "pd.title, " +
                "oi.note, " +
                "SUM(oi.quantity), " +
                "oi.unit," +
                "oi.product.cooking, " +
                "oi.updatedAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "WHERE oi.status = :orderItemStatus " +
            "GROUP BY oi.table.id, oi.product.id, oi.note "
    )
    List<OrderItemKitchenWaiterDTO> getOrderItemWaiterGroupByTableAndProduct(@Param("orderItemStatus") EOrderItemStatus orderItemStatus);


    @Query(value = "SELECT * FROM vw_get_order_item_waiter_group_by_table_and_product", nativeQuery = true)
    List<IOrderItemKitchenWaiterDTO> getOrderItemByStatusWaiterGroupByTableAndProduct();


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO (" +
                "oi.id, " +
                "tb.name, " +
                "pd.id, " +
                "pd.title, " +
                "oi.note, " +
                "oi.quantity, " +
                "oi.unit, " +
                "oi.product.cooking, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "JOIN AppTable As tb " +
            "ON oi.table.id = tb.id " +
            "WHERE oi.status = :orderItemStatus " +
            "AND oi.table.id = :tableId " +
            "ORDER BY oi.updatedAt ASC "
    )
    List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(@Param("orderItemStatus") EOrderItemStatus orderItemStatus, @Param("tableId") Long tableId);


    @Query(value = "CALL sp_get_order_item_by_status_cooking_and_table(:tableId)", nativeQuery = true)
    List<IOrderItemKitchenTableDTO> getOrderItemByStatusCookingAndTable(@Param("tableId") Long tableId);


    @Query("SELECT oi " +
            "FROM OrderItem AS oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.product.id = :productId " +
            "AND oi.status = :orderItemStatus"
    )
    Optional<OrderItem> findByOrderIdAndProductIdAndOrderItemStatus(@Param("orderId") Long orderId, @Param("productId") Long productId, @Param("orderItemStatus") EOrderItemStatus orderItemStatus);


    @Query("SELECT oi " +
            "FROM OrderItem AS oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.status = :orderItemStatus"
    )
    Optional<OrderItem> findByStatus(@Param("orderId") Long orderId, @Param("orderItemStatus") EOrderItemStatus orderItemStatus);


    @Query("SELECT oi " +
            "FROM OrderItem AS oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.table.id = :tableId " +
            "AND oi.status = :orderItemStatus"
    )
    Optional<OrderItem> findByOrderIdAndTableIdAndOrderItemStatus(@Param("orderId") Long orderId, @Param("tableId") Long tableId, @Param("orderItemStatus") EOrderItemStatus orderItemStatus);

    @Query("SELECT NEW com.cg.domain.dto.report.ProductReportDTO (" +
                "pd.id," +
                "pd.title, " +
                "pd.productAvatar.fileFolder, " +
                "pd.productAvatar.fileName, " +
                "SUM(oi.quantity), " +
                "SUM(oi.amount) " +

//            "COALESCE (SUM(oi.quantity) , 0) , " +
//            "case when SUM(oi.amount) is null then 0.0 else SUM(oi.amount) end " +
            ") " +
            "FROM Product AS pd " +
            "LEFT JOIN OrderItem AS oi " +
//            "FROM OrderItem AS oi " +
//            "JOIN Product AS pd " +
            "ON pd.id = oi.product.id " +
            "WHERE (MONTH(Date_Format(oi.createdAt,'%Y/%m/%d')) = :month or oi.createdAt is null) " +
            "AND (YEAR(Date_Format(oi.createdAt,'%Y/%m/%d')) = :year or oi.createdAt is null) " +
            "AND pd.deleted = false " +
            "AND pd.category.id <> 2 " +
            "GROUP BY pd.id " +
            "ORDER BY SUM(oi.quantity) DESC "
    )
    List<ProductReportDTO> getTop5ProductBestSell(Pageable pageable, @Param("month") int month, @Param("year") int year);


    @Query(value = "SELECT * FROM vw_get_top5_best_sell_current_month", nativeQuery = true)
    List<IProductReportDTO> getTop5BestSellCurrentMonth();


    @Query(value = "SELECT * FROM vw_get_top5_unmarket_table_current_month", nativeQuery = true)
    List<IProductReportDTO> getTop5ProductUnMarketTableCurrentMonth();

    @Query("SELECT NEW com.cg.domain.dto.report.ProductReportDTO (" +
                "pd.id," +
                "pd.title, " +
                "pd.productAvatar.fileFolder, " +
                "pd.productAvatar.fileName, " +
            "SUM(oi.quantity), " +
            "SUM(oi.amount) " +

//            "COALESCE (SUM(oi.quantity) , 0) , " +
//            "case when SUM(oi.amount) is null then 0.0 else SUM(oi.amount) end " +
            ") " +
            "FROM Product AS pd " +
            "LEFT JOIN OrderItem AS oi " +
//            "FROM OrderItem AS oi " +
//            "JOIN Product AS pd " +
            "ON pd.id = oi.product.id " +
            "WHERE (MONTH(Date_Format(oi.createdAt,'%Y/%m/%d')) = :month or oi.createdAt is null) " +
            "AND (YEAR(Date_Format(oi.createdAt,'%Y/%m/%d')) = :year or oi.createdAt is null) " +
            "AND pd.deleted = false " +
            "AND pd.category.id <> 2 " +
            "GROUP BY pd.id " +
            "ORDER BY SUM(oi.quantity) ASC "
    )
    List<ProductReportDTO> getTop5ProductUnmarketable(Pageable pageable, @Param("month") int month, @Param("year") int year);
}

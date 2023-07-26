package com.cg.service.orderItem;

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
import com.cg.exception.DataInputException;
import com.cg.repository.OrderItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.repository.TableRepository;
import com.cg.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class OrderItemServiceImpl implements IOrderItemService {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TableRepository tableRepository;

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getById(Long id) {
        return orderItemRepository.getById(id);
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void delete(OrderItem orderItem) {

    }

    @Override
    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItemResDTO>  getOrderItemResDTOByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemResDTOByOrderId(orderId);
    }

    @Override
    public List<OrderItemCurrentDTO> getOrderItemResDTOByOrderIdAndOrderItemStatus(Long orderId, EOrderItemStatus orderItemStatus) {
        return orderItemRepository.getOrderItemResponseDTOByOrderIdAndOrderItemStatus(orderId,orderItemStatus);
    }

    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }

    @Override
    public List<OrderItem> findAllByOrderAndStatus(Order order, EOrderItemStatus status) {
        return orderItemRepository.findAllByOrderAndStatus(order, status);
    }

    @Override
    public List<OrderItemDTO> getOrderItemDTOByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemDTOByOrderId(orderId);
    }

    @Override
    public List<OrderItemBillHistoryDTO> getOrderItemDTOSumByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemDTOSumByOrderId(orderId);
    }

    @Override
    public List<OrderItem> getAllByOrder(Order order) {
        return orderItemRepository.getAllByOrder(order);
    }

    @Override
    public List<OrderItemBillResDTO> getOrderItemBillResDTOByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemBillResDTOByOrderId(orderId);
    }

    @Override
    public Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusWaiter(Long orderId, Long productId, String note) {
        return orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(orderId, productId, note, EOrderItemStatus.WAITER);
    }

    @Override
    public Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusDelivery(Long orderId, Long productId, String note) {
        return orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(orderId, productId, note, EOrderItemStatus.DELIVERY);
    }

    @Override
    public Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusStockOut(Long orderId, Long productId, String note) {
        return orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(orderId, productId, note, EOrderItemStatus.STOCK_OUT);
    }

    @Override
    public Optional<OrderItem> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note) {
        return orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(orderId, productId, note, EOrderItemStatus.DONE);
    }

    @Override
    public List<OrderItemKitchenGroupDTO> getOrderItemByStatusGroupByProduct(EOrderItemStatus status) {
        return orderItemRepository.getOrderItemByStatusGroupByProduct(status);
    }

    @Override
    public List<IOrderItemKitchenGroupDTO> getOrderItemByStatusCookingGroupByProduct() {
        return orderItemRepository.getOrderItemByStatusCookingGroupByProduct();
    }

    @Override
    public List<OrderItemKitchenWaiterDTO> getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus status) {
        return orderItemRepository.getOrderItemWaiterGroupByTableAndProduct(status);
    }

    @Override
    public List<IOrderItemKitchenWaiterDTO> getOrderItemByStatusWaiterGroupByTableAndProduct() {
        return orderItemRepository.getOrderItemByStatusWaiterGroupByTableAndProduct();
    }

    @Override
    public List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(EOrderItemStatus orderItemStatus, Long tableId) {
        return orderItemRepository.getOrderItemByStatusAndTable(orderItemStatus, tableId);
    }

    @Override
    public List<IOrderItemKitchenTableDTO> getOrderItemByStatusCookingAndTable(Long tableId) {
        return orderItemRepository.getOrderItemByStatusCookingAndTable(tableId);
    }

    public Optional<OrderItem> findByProductAndStatusDelivery(Long orderId, Long productId) {
        return orderItemRepository.findByOrderIdAndProductIdAndOrderItemStatus(orderId, productId, EOrderItemStatus.DELIVERY);
    }

    public Optional<OrderItem> findByProductAndStatusDone(Long orderId, Long productId) {
        return orderItemRepository.findByOrderIdAndProductIdAndOrderItemStatus(orderId, productId, EOrderItemStatus.DONE);
    }

    @Override
    public OrderItemKitchenWaiterDTO changeStatusFromCookingToWaiterOfProduct(OrderItem orderItemCooking) {
        Order order = orderItemCooking.getOrder();
        Product product = orderItemCooking.getProduct();
        String note = orderItemCooking.getNote();

        Optional<OrderItem> orderItemWaiterOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiter(order.getId(), product.getId(), note);

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO;

        if (!orderItemWaiterOptional.isPresent()) {
            if (orderItemCooking.getQuantity() == 1) {
                orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                orderItemRepository.save(orderItemCooking);

                orderItemKitchenWaiterDTO = orderItemCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemCooking.getOrder())
                        .setProduct(orderItemCooking.getProduct())
                        .setProductTitle(orderItemCooking.getProductTitle())
                        .setStatus(EOrderItemStatus.WAITER)
                        .setTable(orderItemCooking.getTable())
                        .setNote(orderItemCooking.getNote())
                        .setPrice(orderItemCooking.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemCooking.getPrice())
                        .setUnit(orderItemCooking.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);

                orderItemKitchenWaiterDTO = newOrderItem.toOrderItemKitchenWaiterDTO();
            }
        }
        else {
            OrderItem orderItemWaiter = orderItemWaiterOptional.get();

            if (orderItemCooking.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemCooking.getId());
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);
            }

            int newQuantityWaiter = orderItemWaiter.getQuantity() + 1;
            BigDecimal newAmountWaiter = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
            orderItemWaiter.setQuantity(newQuantityWaiter);
            orderItemWaiter.setAmount(newAmountWaiter);
            orderItemRepository.save(orderItemWaiter);

            orderItemKitchenWaiterDTO = orderItemWaiter.toOrderItemKitchenWaiterDTO();
        }

        return orderItemKitchenWaiterDTO;
    }

    @Override
    public void changeStatusFromCookingToWaiterToProductOfOrder(OrderItem orderItemCooking) {
        Order order = orderItemCooking.getOrder();
        Product product = orderItemCooking.getProduct();
        String note = orderItemCooking.getNote();

        Optional<OrderItem> orderItemWaiter = this.findByOrderIdAndProductIdAndNoteAndStatusWaiter(order.getId(), product.getId(), note);

        if (orderItemWaiter.isPresent()) {
            if (orderItemCooking.getNote().equals(orderItemWaiter.get().getNote())) {
                int newOrderItemWaiterQuantity = orderItemCooking.getQuantity() + orderItemWaiter.get().getQuantity();
                BigDecimal newAmountWaiter = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newOrderItemWaiterQuantity));
                orderItemCooking.setQuantity(newOrderItemWaiterQuantity);
                orderItemCooking.setAmount(newAmountWaiter);
                orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                orderItemRepository.save(orderItemCooking);
                orderItemRepository.deleteById(orderItemWaiter.get().getId());
            }
            else {
                orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                orderItemRepository.save(orderItemCooking);
            }
        }
        else {
            orderItemCooking.setStatus(EOrderItemStatus.WAITER);
            orderItemRepository.save(orderItemCooking);
        }
    }


    @Override
    public void changeStatusFromCookingToWaiterAllProductOfTable(Order order) {

        String tableName = order.getTable().getName();

        List<OrderItem> orderItemCookingList = this.findAllByOrderAndStatus(order, EOrderItemStatus.COOKING);

        List<OrderItem> orderItemWaiterList = this.findAllByOrderAndStatus(order, EOrderItemStatus.WAITER);

        if(orderItemCookingList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderItemWaiterList.size() == 0) {
            for (OrderItem item : orderItemCookingList) {
                item.setStatus(EOrderItemStatus.WAITER);
            }
            orderItemRepository.saveAll(orderItemCookingList);
        }
        else {
            for (OrderItem orderItemCooking : orderItemCookingList) {
                Optional<OrderItem> orderItemWaiter = orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(order.getId(), orderItemCooking.getProduct().getId(), orderItemCooking.getNote(), EOrderItemStatus.WAITER);

                if (orderItemWaiter.isPresent()) {
                    int newOrderItemQuantity = orderItemCooking.getQuantity() + orderItemWaiter.get().getQuantity();
                    orderItemWaiter.get().setQuantity(newOrderItemQuantity);
                    BigDecimal newAmountItemQuantity = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newOrderItemQuantity));
                    orderItemWaiter.get().setAmount(newAmountItemQuantity);
                    orderItemWaiter.get().setStatus(EOrderItemStatus.WAITER);
                    orderItemRepository.save(orderItemWaiter.get());
                    orderItemRepository.delete(orderItemCooking);
                }
                else {
                    orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                    orderItemRepository.save(orderItemCooking);
                }
            }
        }
    }

    @Override
    public OrderItemKitchenWaiterDTO changeStatusFromCookingToStockOutOfProduct(OrderItem orderItemCooking) {
        Order order = orderItemCooking.getOrder();
        Product product = orderItemCooking.getProduct();
        String note = orderItemCooking.getNote();

        Optional<OrderItem> orderItemStockOutOptional = this.findByOrderIdAndProductIdAndNoteAndStatusStockOut(order.getId(), product.getId(), note);

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO;

        if (!orderItemStockOutOptional.isPresent()) {
            if (orderItemCooking.getQuantity() == 1) {
                orderItemCooking.setStatus(EOrderItemStatus.STOCK_OUT);
                orderItemRepository.save(orderItemCooking);

                orderItemKitchenWaiterDTO = orderItemCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemCooking.getOrder())
                        .setProduct(orderItemCooking.getProduct())
                        .setProductTitle(orderItemCooking.getProductTitle())
                        .setStatus(EOrderItemStatus.STOCK_OUT)
                        .setTable(orderItemCooking.getTable())
                        .setNote(orderItemCooking.getNote())
                        .setPrice(orderItemCooking.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemCooking.getPrice())
                        .setUnit(orderItemCooking.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);

                orderItemKitchenWaiterDTO = newOrderItem.toOrderItemKitchenWaiterDTO();
            }
        }
        else {
            OrderItem orderItemStockOut = orderItemStockOutOptional.get();

            if (orderItemCooking.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemCooking.getId());
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);
            }

            int newQuantityStockOut = orderItemStockOut.getQuantity() + 1;
            BigDecimal newAmountStockOut = orderItemStockOut.getPrice().multiply(BigDecimal.valueOf(newQuantityStockOut));
            orderItemStockOut.setQuantity(newQuantityStockOut);
            orderItemStockOut.setAmount(newAmountStockOut);
            orderItemRepository.save(orderItemStockOut);

            orderItemKitchenWaiterDTO = orderItemStockOut.toOrderItemKitchenWaiterDTO();
        }

        return orderItemKitchenWaiterDTO;
    }

    @Override
    public void changeStatusFromCookingToStockOutToProduct(OrderItem orderItemCooking) {
        Order order = orderItemCooking.getOrder();
        Product product = orderItemCooking.getProduct();
        String note = orderItemCooking.getNote();

        Optional<OrderItem> orderItemStockOut = this.findByOrderIdAndProductIdAndNoteAndStatusStockOut(order.getId(), product.getId(), note);

        if (orderItemStockOut.isPresent()) {
            int newOrderItemStockOutQuantity = orderItemCooking.getQuantity() + orderItemStockOut.get().getQuantity();
            BigDecimal newAmountStockOut = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newOrderItemStockOutQuantity));
            orderItemCooking.setQuantity(newOrderItemStockOutQuantity);
            orderItemCooking.setAmount(newAmountStockOut);
            orderItemCooking.setStatus(EOrderItemStatus.STOCK_OUT);
            orderItemRepository.save(orderItemCooking);
            orderItemRepository.deleteById(orderItemStockOut.get().getId());
        }
        else {
            orderItemCooking.setStatus(EOrderItemStatus.STOCK_OUT);
            orderItemRepository.save(orderItemCooking);
        }
    }

    @Override
    public OrderItemKitchenWaiterDTO changeStatusFromCookingToWaiterToOneProductOfGroup(Long productId, String note) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductIdAndNoteAndStatusOrderByIdAsc(productId, note, EOrderItemStatus.COOKING);

        if (orderItems.size() == 0) {
            throw new DataInputException("Không tìm thấy hóa đơn theo sản phẩm với trạng thái tương ứng");
        }

        OrderItem orderItemCooking = orderItems.get(0);

        Optional<OrderItem> orderItemWaiterOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiter(orderItemCooking.getOrder().getId(), productId, note);

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO;

        if (!orderItemWaiterOptional.isPresent()) {
            if (orderItemCooking.getQuantity() == 1) {
                orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                orderItemRepository.save(orderItemCooking);

                orderItemKitchenWaiterDTO = orderItemCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemCooking.getOrder())
                        .setProduct(orderItemCooking.getProduct())
                        .setProductTitle(orderItemCooking.getProductTitle())
                        .setStatus(EOrderItemStatus.WAITER)
                        .setTable(orderItemCooking.getTable())
                        .setNote(orderItemCooking.getNote())
                        .setPrice(orderItemCooking.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemCooking.getPrice())
                        .setUnit(orderItemCooking.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);

                orderItemKitchenWaiterDTO = newOrderItem.toOrderItemKitchenWaiterDTO();
            }
        }
        else {
            OrderItem orderItemWaiter = orderItemWaiterOptional.get();

            if (orderItemCooking.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemCooking.getId());
            }
            else {
                int newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderItemRepository.save(orderItemCooking);
            }

            int newQuantityWaiter = orderItemWaiter.getQuantity() + 1;
            BigDecimal newAmountWaiter = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
            orderItemWaiter.setQuantity(newQuantityWaiter);
            orderItemWaiter.setAmount(newAmountWaiter);
            orderItemRepository.save(orderItemWaiter);

            orderItemKitchenWaiterDTO = orderItemWaiter.toOrderItemKitchenWaiterDTO();
        }

        return orderItemKitchenWaiterDTO;
    }

    @Override
    public List<OrderItemKitchenWaiterDTO> changeStatusFromCookingToWaiterToAllProductsOfGroup(Long productId, String note) {
        List<OrderItem> orderItemsCooking = orderItemRepository.findAllByProductIdAndNoteAndStatus(productId, note, EOrderItemStatus.COOKING);

        if (orderItemsCooking.size() == 0) {
            throw new DataInputException("Không tìm thấy hóa đơn theo sản phẩm với trạng thái tương ứng");
        }

        List<OrderItemKitchenWaiterDTO> orderItemKitchenWaiterDTOList = new ArrayList<>();

        for (OrderItem orderItemCooking : orderItemsCooking) {
            Optional<OrderItem> orderItemWaiterOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiter(orderItemCooking.getOrder().getId(), productId, note);

            if (!orderItemWaiterOptional.isPresent()) {
                orderItemCooking.setStatus(EOrderItemStatus.WAITER);
                orderItemRepository.save(orderItemCooking);

                OrderItemKitchenWaiterDTO item = orderItemCooking.toOrderItemKitchenWaiterDTO();
                orderItemKitchenWaiterDTOList.add(item);
            }
            else {
                OrderItem orderItemWaiter = orderItemWaiterOptional.get();

                int newQuantityWaiter = orderItemWaiter.getQuantity() + orderItemCooking.getQuantity();
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderItemWaiter.setQuantity(newQuantityWaiter);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemRepository.save(orderItemWaiter);

                OrderItemKitchenWaiterDTO item = orderItemWaiter.toOrderItemKitchenWaiterDTO();
                orderItemKitchenWaiterDTOList.add(item);

                orderItemRepository.delete(orderItemCooking);
            }
        }

        return orderItemKitchenWaiterDTOList;
    }

    @Override
    public void changeStatusFromWaiterToDeliveryOfProduct(OrderItem orderItemWaiter) {
        Order order = orderItemWaiter.getOrder();
        Product product = orderItemWaiter.getProduct();
        String note = orderItemWaiter.getNote();

        Optional<OrderItem> orderItemDeliveryOptional = this.findByOrderIdAndProductIdAndNoteAndStatusDelivery(order.getId(), product.getId(), note);

        if (!orderItemDeliveryOptional.isPresent()) {
            if (orderItemWaiter.getQuantity() == 1) {
                orderItemWaiter.setStatus(EOrderItemStatus.DELIVERY);
                orderItemRepository.save(orderItemWaiter);
            }
            else {
                int newQuantityWaiter = orderItemWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderItemWaiter.setQuantity(newQuantityWaiter);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemRepository.save(orderItemWaiter);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemWaiter.getOrder())
                        .setProduct(orderItemWaiter.getProduct())
                        .setProductTitle(orderItemWaiter.getProductTitle())
                        .setStatus(EOrderItemStatus.DELIVERY)
                        .setTable(orderItemWaiter.getTable())
                        .setNote(orderItemWaiter.getNote())
                        .setPrice(orderItemWaiter.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemWaiter.getPrice())
                        .setUnit(orderItemWaiter.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);
            }
        }
        else {
            OrderItem orderItemDelivery = orderItemDeliveryOptional.get();

            if (orderItemWaiter.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemWaiter.getId());
            }
            else {
                int newQuantityWaiter = orderItemWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderItemWaiter.setQuantity(newQuantityWaiter);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemRepository.save(orderItemWaiter);
            }

            int newQuantityDelivery = orderItemDelivery.getQuantity() + 1;
            BigDecimal newAmountDelivery = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
            orderItemDelivery.setQuantity(newQuantityDelivery);
            orderItemDelivery.setAmount(newAmountDelivery);
            orderItemRepository.save(orderItemDelivery);
        }
    }

    @Override
    public void changeStatusFromWaiterToDeliveryToProductOfOrder(OrderItem orderItemWaiter) {
        Order order = orderItemWaiter.getOrder();
        Product product = orderItemWaiter.getProduct();
        String note = orderItemWaiter.getNote();

        Optional<OrderItem> orderItemDelivery = this.findByOrderIdAndProductIdAndNoteAndStatusDelivery(order.getId(), product.getId(), note);

        if (orderItemDelivery.isPresent()) {
            if (orderItemWaiter.getNote().equals(orderItemDelivery.get().getNote())) {
                int newOrderItemWaiterQuantity = orderItemWaiter.getQuantity() + orderItemDelivery.get().getQuantity();
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newOrderItemWaiterQuantity));
                orderItemWaiter.setQuantity(newOrderItemWaiterQuantity);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemWaiter.setStatus(EOrderItemStatus.DELIVERY);
                orderItemRepository.save(orderItemWaiter);
                orderItemRepository.deleteById(orderItemDelivery.get().getId());
            }
            else {
                orderItemWaiter.setStatus(EOrderItemStatus.DELIVERY);
                orderItemRepository.save(orderItemWaiter);
            }
        }
        else {
            orderItemWaiter.setStatus(EOrderItemStatus.DELIVERY);
            orderItemRepository.save(orderItemWaiter);
        }
    }

    @Override
    public void changeStatusFromWaiterToDeliveryAllProductOfTable(Order order) {
        String tableName = order.getTable().getName();

        List<OrderItem> orderItemWaiterList = this.findAllByOrderAndStatus(order, EOrderItemStatus.WAITER);

        List<OrderItem> orderItemDeliveryList = this.findAllByOrderAndStatus(order, EOrderItemStatus.DELIVERY);

        if(orderItemWaiterList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderItemDeliveryList.size() == 0) {
            for (OrderItem item : orderItemWaiterList) {
                item.setStatus(EOrderItemStatus.DELIVERY);
            }
            orderItemRepository.saveAll(orderItemWaiterList);
        }
        else {
            for (OrderItem orderItemWaiter : orderItemWaiterList) {
                Optional<OrderItem> orderItemDelivery = orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(order.getId(), orderItemWaiter.getProduct().getId(), orderItemWaiter.getNote(), EOrderItemStatus.DELIVERY);

                if (orderItemDelivery.isPresent()) {
                    int newOrderItemQuantity = orderItemWaiter.getQuantity() + orderItemDelivery.get().getQuantity();
                    orderItemDelivery.get().setQuantity(newOrderItemQuantity);
                    BigDecimal newAmountItemQuantity = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newOrderItemQuantity));
                    orderItemDelivery.get().setAmount(newAmountItemQuantity);
                    orderItemDelivery.get().setStatus(EOrderItemStatus.DELIVERY);
                    orderItemRepository.save(orderItemDelivery.get());
                    orderItemRepository.delete(orderItemWaiter);
                }
                else {
                    orderItemWaiter.setStatus(EOrderItemStatus.DELIVERY);
                    orderItemRepository.save(orderItemWaiter);
                }
            }
        }
    }

    @Override
    public void changeStatusFromWaiterToStockOutOfProduct(OrderItem orderItemWaiter) {
        Order order = orderItemWaiter.getOrder();
        Product product = orderItemWaiter.getProduct();
        String note = orderItemWaiter.getNote();

        Optional<OrderItem> orderItemStockOutOptional = this.findByOrderIdAndProductIdAndNoteAndStatusStockOut(order.getId(), product.getId(), note);

        if (!orderItemStockOutOptional.isPresent()) {
            if (orderItemWaiter.getQuantity() == 1) {
                orderItemWaiter.setStatus(EOrderItemStatus.STOCK_OUT);
                orderItemRepository.save(orderItemWaiter);
            }
            else {
                int newQuantityWaiter = orderItemWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderItemWaiter.setQuantity(newQuantityWaiter);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemRepository.save(orderItemWaiter);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemWaiter.getOrder())
                        .setProduct(orderItemWaiter.getProduct())
                        .setProductTitle(orderItemWaiter.getProductTitle())
                        .setStatus(EOrderItemStatus.STOCK_OUT)
                        .setTable(orderItemWaiter.getTable())
                        .setNote(orderItemWaiter.getNote())
                        .setPrice(orderItemWaiter.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemWaiter.getPrice())
                        .setUnit(orderItemWaiter.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);
            }
        }
        else {
            OrderItem orderItemDelivery = orderItemStockOutOptional.get();

            if (orderItemWaiter.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemWaiter.getId());
            }
            else {
                int newQuantityWaiter = orderItemWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderItemWaiter.setQuantity(newQuantityWaiter);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemRepository.save(orderItemWaiter);
            }

            int newQuantityDelivery = orderItemDelivery.getQuantity() + 1;
            BigDecimal newAmountDelivery = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
            orderItemDelivery.setQuantity(newQuantityDelivery);
            orderItemDelivery.setAmount(newAmountDelivery);
            orderItemRepository.save(orderItemDelivery);
        }
    }

    @Override
    public void changeStatusFromWaiterToStockOutToProductOfOrder(OrderItem orderItemWaiter) {
        Order order = orderItemWaiter.getOrder();
        Product product = orderItemWaiter.getProduct();
        String note = orderItemWaiter.getNote();

        Optional<OrderItem> orderItemDelivery = this.findByOrderIdAndProductIdAndNoteAndStatusStockOut(order.getId(), product.getId(), note);

        if (orderItemDelivery.isPresent()) {
            if (orderItemWaiter.getNote().equals(orderItemDelivery.get().getNote())) {
                int newOrderItemWaiterQuantity = orderItemWaiter.getQuantity() + orderItemDelivery.get().getQuantity();
                BigDecimal newAmountWaiter = orderItemWaiter.getPrice().multiply(BigDecimal.valueOf(newOrderItemWaiterQuantity));
                orderItemWaiter.setQuantity(newOrderItemWaiterQuantity);
                orderItemWaiter.setAmount(newAmountWaiter);
                orderItemWaiter.setStatus(EOrderItemStatus.STOCK_OUT);
                orderItemRepository.save(orderItemWaiter);
                orderItemRepository.deleteById(orderItemDelivery.get().getId());
            }
            else {
                orderItemWaiter.setStatus(EOrderItemStatus.STOCK_OUT);
                orderItemRepository.save(orderItemWaiter);
            }
        }
        else {
            orderItemWaiter.setStatus(EOrderItemStatus.STOCK_OUT);
            orderItemRepository.save(orderItemWaiter);
        }
    }

    @Override
    public void changeStatusFromDeliveryToDoneOfProduct(OrderItem orderItemDelivery) {
        Order order = orderItemDelivery.getOrder();
        Product product = orderItemDelivery.getProduct();
        String note = orderItemDelivery.getNote();

        Optional<OrderItem> orderItemDoneOptional = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);

        if (!orderItemDoneOptional.isPresent()) {
            if (orderItemDelivery.getQuantity() == 1) {
                orderItemDelivery.setStatus(EOrderItemStatus.DONE);
                orderItemRepository.save(orderItemDelivery);
            }
            else {
                int newQuantityDelivery = orderItemDelivery.getQuantity() - 1;
                BigDecimal newAmountDelivery = orderItemDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
                orderItemDelivery.setQuantity(newQuantityDelivery);
                orderItemDelivery.setAmount(newAmountDelivery);
                orderItemRepository.save(orderItemDelivery);

                OrderItem newOrderItem = new OrderItem()
                        .setOrder(orderItemDelivery.getOrder())
                        .setProduct(orderItemDelivery.getProduct())
                        .setProductTitle(orderItemDelivery.getProductTitle())
                        .setStatus(EOrderItemStatus.DONE)
                        .setTable(orderItemDelivery.getTable())
                        .setNote(orderItemDelivery.getNote())
                        .setPrice(orderItemDelivery.getPrice())
                        .setQuantity(1)
                        .setAmount(orderItemDelivery.getPrice())
                        .setUnit(orderItemDelivery.getUnit())
                        ;
                orderItemRepository.save(newOrderItem);
            }
        }
        else {
            OrderItem orderItemDone = orderItemDoneOptional.get();

            if (orderItemDelivery.getQuantity() == 1) {
                orderItemRepository.deleteById(orderItemDelivery.getId());
            }
            else {
                int newQuantityDelivery = orderItemDelivery.getQuantity() - 1;
                BigDecimal newAmountDelivery = orderItemDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
                orderItemDelivery.setQuantity(newQuantityDelivery);
                orderItemDelivery.setAmount(newAmountDelivery);
                orderItemRepository.save(orderItemDelivery);
            }

            int newQuantityDone = orderItemDone.getQuantity() + 1;
            BigDecimal newAmountDone = orderItemDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDone));
            orderItemDone.setQuantity(newQuantityDone);
            orderItemDone.setAmount(newAmountDone);
            orderItemRepository.save(orderItemDone);
        }
    }

    @Override
    public void changeStatusFromDeliveryToDoneToProductOfOrder(OrderItem orderItemDelivery) {
        Order order = orderItemDelivery.getOrder();
        Product product = orderItemDelivery.getProduct();
        String note = orderItemDelivery.getNote();

        Optional<OrderItem> orderItemDone = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);

        if (orderItemDone.isPresent()) {
            if (orderItemDelivery.getNote().equals(orderItemDone.get().getNote())) {
                int newOrderItemDeliveryQuantity = orderItemDelivery.getQuantity() + orderItemDone.get().getQuantity();
                BigDecimal newAmountDelivery = orderItemDelivery.getPrice().multiply(BigDecimal.valueOf(newOrderItemDeliveryQuantity));
                orderItemDelivery.setQuantity(newOrderItemDeliveryQuantity);
                orderItemDelivery.setAmount(newAmountDelivery);
                orderItemDelivery.setStatus(EOrderItemStatus.DONE);
                orderItemRepository.save(orderItemDelivery);
                orderItemRepository.deleteById(orderItemDone.get().getId());
            }
            else {
                orderItemDelivery.setStatus(EOrderItemStatus.DONE);
                orderItemRepository.save(orderItemDelivery);
            }
        }
        else {
            orderItemDelivery.setStatus(EOrderItemStatus.DONE);
            orderItemRepository.save(orderItemDelivery);
        }
    }

    @Override
    public void changeStatusFromDeliveryToDoneToTableAll(Order order) {
        String tableName = order.getTable().getName();

        List<OrderItem> orderItemDeliveryList = this.findAllByOrderAndStatus(order, EOrderItemStatus.DELIVERY);

        List<OrderItem> orderItemDoneList = this.findAllByOrderAndStatus(order, EOrderItemStatus.DONE);

        if(orderItemDeliveryList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderItemDoneList.size() == 0) {
            for (OrderItem item : orderItemDeliveryList) {
                item.setStatus(EOrderItemStatus.DONE);
            }
            orderItemRepository.saveAll(orderItemDeliveryList);
        }
        else {
            for (OrderItem orderItemDelivery : orderItemDeliveryList) {
                Optional<OrderItem> orderItemDone = orderItemRepository.findByOrderIdAndProductIdAndNoteAndOrderItemStatus(order.getId(), orderItemDelivery.getProduct().getId(), orderItemDelivery.getNote(), EOrderItemStatus.DONE);

                if (orderItemDone.isPresent()) {
                    int newOrderItemQuantity = orderItemDelivery.getQuantity() + orderItemDone.get().getQuantity();
                    orderItemDone.get().setQuantity(newOrderItemQuantity);
                    BigDecimal newAmountItemQuantity = orderItemDelivery.getPrice().multiply(BigDecimal.valueOf(newOrderItemQuantity));
                    orderItemDone.get().setAmount(newAmountItemQuantity);
                    orderItemDone.get().setStatus(EOrderItemStatus.DONE);
                    orderItemRepository.save(orderItemDone.get());
                    orderItemRepository.delete(orderItemDelivery);
                }
                else {
                    orderItemDelivery.setStatus(EOrderItemStatus.DONE);
                    orderItemRepository.save(orderItemDelivery);
                }
            }
        }
    }

    @Override
    public void deleteOrderItemStockOut(OrderItem orderItem) {
        Order order = orderItem.getOrder();

        orderItemRepository.delete(orderItem);

        //tính lại tổng giá tiền của order
        BigDecimal newTotalAmount = iOrderService.calculateTotalAmount(order.getId());
        order.setTotalAmount(newTotalAmount);
        orderRepository.save(order);
    }

    @Override
    public List<ProductReportDTO> getTop5ProductBestSell(int month, int year, Pageable pageable) {
        return orderItemRepository.getTop5ProductBestSell(pageable,month, year);
    }

    @Override
    public List<ProductReportDTO> getTop5ProductUnmarketable(int month, int year, Pageable pageable) {
        return orderItemRepository.getTop5ProductUnmarketable(pageable,month, year);
    }

    @Override
    public List<IProductReportDTO> getTop5BestSellCurrentMonth() {
        return orderItemRepository.getTop5BestSellCurrentMonth();
    }

    @Override
    public List<IProductReportDTO> getTop5ProductUnMarketTableCurrentMonth() {
        return orderItemRepository.getTop5ProductUnMarketTableCurrentMonth();
    }
}

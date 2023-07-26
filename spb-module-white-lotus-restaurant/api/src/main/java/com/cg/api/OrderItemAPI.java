package com.cg.api;

import com.cg.domain.dto.order.OrderKitchenTableDTO;
import com.cg.domain.dto.orderItem.*;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.utils.ValidateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/order-items")
public class OrderItemAPI {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private ValidateUtils validateUtils;


    // Lấy tất cả danh sách sản phẩm cho bếp
    @GetMapping("/kitchen/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CASHIER', 'KITCHEN', 'WAITER')")
    public ResponseEntity<?> getAll() {
        Map<String, List<?>> result = getAllItems();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public Map<String, List<?>> getAllItems() {
        //        List<OrderItemKitchenGroupDTO> itemsCooking = iOrderItemService.getOrderItemByStatusGroupByProduct(EOrderItemStatus.COOKING);
        List<IOrderItemKitchenGroupDTO> itemsCooking = iOrderItemService.getOrderItemByStatusCookingGroupByProduct();

        List<OrderKitchenTableDTO> itemsTable = iOrderService.getAllOrderKitchenCookingByTable(EOrderItemStatus.COOKING);

//        List<OrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);
        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        Map<String, List<?>> result = new HashMap<>();

        result.put("itemsCooking", itemsCooking);
        result.put("itemsTable", itemsTable);
        result.put("itemsWaiter", itemsWaiter);

        return result;
    }

    // Lấy danh sách sản phẩm theo nhóm sản phẩm cho bếp
    @GetMapping("/kitchen/get-by-status-cooking")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusCooking() {

        List<OrderItemKitchenGroupDTO> orderItemList = iOrderItemService.getOrderItemByStatusGroupByProduct(EOrderItemStatus.COOKING);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItemList, HttpStatus.OK);
    }

    //lấy ra danh sách orderitem có trạng thái cooking gộp theo bàn
    @GetMapping("/kitchen/get-by-status-cooking-group-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusCookingGroupTable() {

        List<OrderKitchenTableDTO> orderList = iOrderService.getAllOrderKitchenCookingByTable(EOrderItemStatus.COOKING);

        if (orderList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    // Lấy danh sách sản phẩm đã nấu xong cho phục vụ bàn
    @GetMapping("/kitchen/get-by-status-waiter")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusWaiter() {

        List<OrderItemKitchenWaiterDTO> orderItemList = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItemList, HttpStatus.OK);
    }


    // Chuyển trạng thái 1 sản phẩm của bàn từ cooking sang waiter
    @PostMapping("/kitchen/table/change-status-cooking-to-waiter")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterOfProduct(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.COOKING)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO = iOrderItemService.changeStatusFromCookingToWaiterOfProduct(orderItem);

//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderItemKitchenWaiterDTO, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ cooking sang waiter
    @PostMapping("/kitchen/table/change-status-cooking-to-waiter-to-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToProductOfOrder(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.COOKING)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromCookingToWaiterToProductOfOrder(orderItem);

//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        Map<String, List<?>> result = getAllItems();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ sản phẩm trong bàn từ cooking sang waiter
    @PostMapping("/kitchen/table/change-status-cooking-to-waiter-all-products")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterAllProductOfTable(@RequestParam("orderId") String orderStr) {

        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = iOrderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không tồn tại");
        });

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromCookingToWaiterAllProductOfTable(order);

        Map<String, List<?>> result = getAllItems();

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //Chuyển trạng thái cooking sang waiter 1 sản phẩm theo nhóm sản phẩm
    @PostMapping("/kitchen/product/change-status-cooking-to-waiter-one-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToOneProductOfGroup(HttpServletRequest request) throws IOException {

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String productIdStr;
        String note;

        try {
            productIdStr = json.get("productId").asText();
            note = Objects.equals(json.get("note").asText(), "null") ? null : json.get("note").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("ID sản phẩm phải là ký tự số");
        }

        Long productId = Long.parseLong(productIdStr);

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO = iOrderItemService.changeStatusFromCookingToWaiterToOneProductOfGroup(productId, note);
//        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(orderItemKitchenWaiterDTO, HttpStatus.OK);
    }

    //Chuyển trạng thái cooking sang waiter theo nhóm sản phẩm
    @PostMapping("/kitchen/product/change-status-cooking-to-waiter-all-products")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToAllProductsOfGroup(HttpServletRequest request) throws IOException {

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String productIdStr;
        String note;

        try {
            productIdStr = json.get("productId").asText();
            note = Objects.equals(json.get("note").asText(), "null") ? null : json.get("note").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("ID sản phẩm phải là ký tự số");
        }

        Long productId = Long.parseLong(productIdStr);

        iOrderItemService.changeStatusFromCookingToWaiterToAllProductsOfGroup(productId, note);

//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());
//        List<OrderItemKitchenWaiterDTO> orderItemKitchenWaiterDTO = iOrderItemService.changeStatusFromCookingToWaiterToAllProductsOfGroup(productId, note);

        Map<String, List<?>> result = getAllItems();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Chuyển trạng thái 1 sản phẩm của bàn từ waiter sang delivery
    @PostMapping("/kitchen/product/change-status-waiter-to-delivery")
    //    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToDeliveryOfProduct(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.WAITER)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromWaiterToDeliveryOfProduct(orderItem);

//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

//        List<OrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);

        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ waiter sang delivery
    @PostMapping("/kitchen/table/change-status-waiter-to-delivery-to-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToDeliveryToProductOfOrder(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.WAITER)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromWaiterToDeliveryToProductOfOrder(orderItem);

//        List<OrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);
        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ sản phẩm trong bàn từ waiter sang delivery
    @PostMapping("/kitchen/table/change-status-waiter-to-delivery-all-products")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToDeliveryToTableAll(@RequestParam("orderId") String orderStr) {

        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = iOrderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không hợp lệ");
        });

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromWaiterToDeliveryAllProductOfTable(order);

        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderItemResDTOS,HttpStatus.OK);
    }

    // Chuyển trạng thái 1 sản phẩm của bàn từ cooking sang stock out
    @PostMapping("/kitchen/table/change-status-cooking-to-stock-out")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToStockOutOfProduct(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.COOKING)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO = iOrderItemService.changeStatusFromCookingToStockOutOfProduct(orderItem);
//        iOrderItemService.changeStatusFromCookingToStockOutOfProduct(orderItem);
//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

//        List<IOrderItemKitchenGroupDTO> itemsCooking = iOrderItemService.getOrderItemByStatusCookingGroupByProduct();
//        List<OrderKitchenTableDTO> itemsTable = iOrderService.getAllOrderKitchenCookingByTable(EOrderItemStatus.COOKING);
//
//        Map<String, List<?>> result = new HashMap<>();
//
//        result.put("itemsCooking", itemsCooking);
//        result.put("itemsTable", itemsTable);

        return new ResponseEntity<>(orderItemKitchenWaiterDTO, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ cooking sang stock out
    @PostMapping("/kitchen/table/change-status-cooking-to-stock-out-to-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToStockOutToProductOfTable(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.COOKING)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

//        OrderItemKitchenWaiterDTO orderItemKitchenWaiterDTO = iOrderItemService.changeStatusFromCookingToStockOutOfProduct(orderItem);
        iOrderItemService.changeStatusFromCookingToStockOutToProduct(orderItem);
//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        List<IOrderItemKitchenGroupDTO> itemsCooking = iOrderItemService.getOrderItemByStatusCookingGroupByProduct();
        List<OrderKitchenTableDTO> itemsTable = iOrderService.getAllOrderKitchenCookingByTable(EOrderItemStatus.COOKING);

        Map<String, List<?>> result = new HashMap<>();

        result.put("itemsCooking", itemsCooking);
        result.put("itemsTable", itemsTable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Chuyển trạng thái 1 sản phẩm của bàn từ waiter sang stock out
    @PostMapping("/kitchen/product/change-status-waiter-to-stock-out")
    //    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToStockOutOfProduct(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.WAITER)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromWaiterToStockOutOfProduct(orderItem);

//        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

//        List<OrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);

        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ waiter sang stock out
    @PostMapping("/kitchen/table/change-status-waiter-to-stock-out-to-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToStockOutToProductOfOrder(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.WAITER)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromWaiterToStockOutToProductOfOrder(orderItem);

//        List<OrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemWaiterGroupByTableAndProduct(EOrderItemStatus.WAITER);
        List<IOrderItemKitchenWaiterDTO> itemsWaiter = iOrderItemService.getOrderItemByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    // Chuyển trạng thái 1 sản phẩm của bàn từ delivery sang done
    @PostMapping("/kitchen/product/change-status-delivery-to-done")
    //    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneOfProduct(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.DELIVERY)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromDeliveryToDoneOfProduct(orderItem);

        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderItemResDTOS, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ delivery sang done
    @PostMapping("/kitchen/table/change-status-delivery-to-done-to-product")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneToProductOfOrder(@RequestParam("orderItemId") String orderItemStr) {

        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.DELIVERY)) {
            String tableName = orderItem.getTable().getName();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái", tableName));
        }

        Order order = orderItem.getOrder();

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromDeliveryToDoneToProductOfOrder(orderItem);

        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderItemResDTOS,HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ sản phẩm trong bàn từ delivery sang done
    @PostMapping("/kitchen/table/change-status-delivery-to-done-all-products")
//    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneToTableAll(@RequestParam("orderId") String orderStr) {

        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = iOrderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không hợp lệ");
        });

        if (!order.getStatus().equals(EBillStatus.ORDERING)) {
            switch (order.getStatus()) {
                case CANCEL:
                    throw new DataInputException("Hóa đơn này đã được hủy");
                case PAID:
                    throw new DataInputException("Hóa đơn này đã thanh toán");
            }
        }

        iOrderItemService.changeStatusFromDeliveryToDoneToTableAll(order);

        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderItemResDTOS,HttpStatus.OK);
    }


    @DeleteMapping("/cashier/delete-item-stock-out")
    public ResponseEntity<?> deleteItemStockOut(@RequestParam("orderItemId") String orderItemStr) {
        if (!validateUtils.isNumberValid(orderItemStr)) {
            throw new DataInputException("ID sản phẩm phải là ký tự số");
        }

        Long orderItemId = Long.parseLong(orderItemStr);

        OrderItem orderItem = iOrderItemService.findById(orderItemId).orElseThrow(() -> {
            throw new DataInputException("Sản phẩm không tồn tại trong hóa đơn");
        });

        if (!orderItem.getStatus().equals(EOrderItemStatus.STOCK_OUT)) {
            throw new DataInputException("Sản phẩm không hợp lệ");
        }

        iOrderItemService.deleteOrderItemStockOut(orderItem);

        Map<String, Long> result = new HashMap<>();
        result.put("orderItemId", orderItemId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

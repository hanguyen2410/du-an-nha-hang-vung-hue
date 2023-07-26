package com.cg.api;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderItem.OrderItemBillHistoryDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.exception.DataInputException;
import com.cg.exception.TableDuplicateException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/orders")
public class OrderAPI {
    
    @Autowired
    private IOrderService iOderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ITableService iTableService;

    @Autowired
    private IStaffService iStaffService;

    @GetMapping()
    public ResponseEntity<?> getAllOrder(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        Page<OrderResDTO> orderDTOS = iOderService.getAllOrderDTOByDeletedIsFalse(pageable);

        if (orderDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = iOderService.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID đơn đặt hàng không hợp lệ.");
        }

        Order order = orderOptional.get();
        OrderResDTO orderResDTO = order.toOrderDTO();
        return new ResponseEntity<>(orderResDTO, HttpStatus.OK);
    }

    @GetMapping("/list-order-items/{orderId}")
    public ResponseEntity<?> getOrderItemById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = iOderService.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID đơn đặt hàng không hợp lệ.");
        }
        List<OrderItemDTO> orderItemDTOList = iOrderItemService.getOrderItemDTOByOrderId(orderId);
        if (orderItemDTOList.size() == 0) {
            throw new DataInputException("Đơn hàng trống");
        }
        return new ResponseEntity<>(orderItemDTOList, HttpStatus.OK);
    }

    @GetMapping("/list-order-items-sum/{orderId}")
    public ResponseEntity<?> getOrderItemSumById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = iOderService.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID đơn đặt hàng không hợp lệ.");
        }
        List<OrderItemBillHistoryDTO> orderItemDTOList = iOrderItemService.getOrderItemDTOSumByOrderId(orderId);
        if (orderItemDTOList.size() == 0) {
            throw new DataInputException("Đơn hàng trống");
        }
        return new ResponseEntity<>(orderItemDTOList, HttpStatus.OK);
    }

    @GetMapping("/get-all-items/{tableId}")
    public ResponseEntity<?> getOrderByTableId(@PathVariable Long tableId) {

        AppTable appTable = iTableService.findById(tableId).orElseThrow(() -> {
            throw new DataInputException("ID bàn không hợp lệ.");
        });

        List<Order> orders = iOderService.findAllByTableAndStatus(appTable, EBillStatus.ORDERING);

        if (orders.size() > 1) {
            throw new TableDuplicateException("Bàn này có nhiều hóa đơn với trạng thái ORDERING, vui lòng kiểm tra dữ liệu hệ thống");
        }
        else {
            if (orders.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                Order order = orders.get(0);
                List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

                return new ResponseEntity<>(order.toOrderWithOrderItemResDTO(orderItemResDTOS), HttpStatus.OK);
            }
        }
    }

    @PostMapping
    public ResponseEntity<?> addItems(@Validated @RequestBody OrderWithOrderItemReqDTO orderWithOrderItemReqDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long tableId = orderWithOrderItemReqDTO.getTableId();

        AppTable appTable = iTableService.findById(tableId)
            .orElseThrow(() -> new DataInputException("ID bàn không hợp lệ."));

        EOrderItemStatus eOrderItemStatus = EOrderItemStatus.fromString(orderWithOrderItemReqDTO.getStatus().toUpperCase());

        if (!eOrderItemStatus.equals(EOrderItemStatus.NEW) && !eOrderItemStatus.equals(EOrderItemStatus.UPDATE)) {
            throw new DataInputException("Thông tin không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        List<Order> orders = iOderService.findAllByTableAndStatus(appTable, EBillStatus.ORDERING);

        if (orders.size() > 1) {
            throw new TableDuplicateException("Bàn này có nhiều hóa đơn với trạng thái ORDERING, vui lòng kiểm tra dữ liệu hệ thống");
        }
        else {
            if (orders.size() == 0) {
                if (eOrderItemStatus.equals(EOrderItemStatus.UPDATE)) {
                    throw new DataInputException("Bàn này chưa có hóa đơn, vui lòng thay đổi trạng thái order");
                }
                else {
                    OrderAndListTableDTO OrderAndListTableDTO = iOderService.createWithOrderItems(orderWithOrderItemReqDTO, appTable);

                    return new ResponseEntity<>(OrderAndListTableDTO, HttpStatus.CREATED);
                }
            }
            else {
                if (eOrderItemStatus.equals(EOrderItemStatus.NEW)) {
                    throw new DataInputException("Bàn này đã có hóa đơn, vui lòng thay đổi trạng thái order");
                }
                else {
                    Order order = orders.get(0);
                    OrderAndListTableDTO OrderAndListTableDTO = iOderService.updateWithOrderItems(orderWithOrderItemReqDTO, order);
                    return new ResponseEntity<>(OrderAndListTableDTO, HttpStatus.OK);
                }
            }
        }
    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> delete(@PathVariable Long orderId) {
        Optional<Order> orderOptional = iOderService.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID đơn đặt hàng không hợp lệ.");
        }

        try {
            iOderService.softDelete(orderId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new DataInputException("Vui lòng liên hệ Administrator!!");
        }
    }

}

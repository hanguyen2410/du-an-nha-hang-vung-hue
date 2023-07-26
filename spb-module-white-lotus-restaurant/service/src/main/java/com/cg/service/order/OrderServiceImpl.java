package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderItem.*;
import com.cg.domain.entity.backup.BillBackup;
import com.cg.domain.entity.backup.TableBackup;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.*;
import com.cg.service.backupBill.IBackupBillService;
import com.cg.service.backupTable.IBackupTableService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Transactional
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IStaffService iStaffService;
    
    @Autowired
    private IProductService iProductService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private IBackupTableService iBackupTableService;

    @Autowired
    private ITableService iTableService;

    @Autowired
    private IBackupBillService iBackupBillService;

    @Autowired
    private BackupBillRepository backupBillRepository;

    @Autowired
    private TableBackupRepository tableBackupRepository;

    @Autowired
    private AppUtils appUtils;

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.getById(id);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Page<OrderResDTO> getAllOrderDTOByDeletedIsFalse(Pageable pageable) {
        return orderRepository.getAllOrderDTOByDeletedIsFalse(pageable);
    }

    @Override
    public Page<OrderResDTO> getAllOrderDTOByDayToDay(String startDay, String endDay, Pageable pageable) {
        return orderRepository.getAllOrderDTOByDayToDay(startDay, endDay, pageable);
    }

    @Override
    public List<Order> getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }

    @Override
    public List<OrderDTO> getOrderDTOByStatus(EBillStatus orderStatus) {
        return orderRepository.getOrderDTOByStatus(orderStatus);
    }

    @Override
    public List<IOrderDTO> getOrderDTOByStatusCooking() {
        return orderRepository.getOrderDTOByStatusCooking();
    }

    @Override
    public int countProductInOrder(List<OrderItemKitchenTableDTO> orderItemList) {
        int count = 0;
        for (OrderItemKitchenTableDTO item : orderItemList) {
            count += item.getQuantity();
        }
        return count;
    }

    @Override
    public int countProductInOrderItem(List<IOrderItemKitchenTableDTO> orderItemList) {
        int count = 0;
        for (IOrderItemKitchenTableDTO item : orderItemList) {
            count += item.getQuantity();
        }
        return count;
    }

    @Override
    public Optional<Order> findByTableAndStatus(AppTable appTable, EBillStatus eBillStatus) {
        return orderRepository.findByTableAndStatus(appTable, eBillStatus);
    }

    @Override
    public List<Order> findAllByTableAndStatus(AppTable appTable, EBillStatus eBillStatus) {
        return orderRepository.findAllByTableAndStatus(appTable, eBillStatus);
    }

    @Override
    public List<OrderKitchenTableDTO> getAllOrderKitchenCookingByTable(EOrderItemStatus orderItemStatus) {
        List<OrderKitchenTableDTO> orderList = new ArrayList<>();

//        List<OrderDTO> orderDTOList = getOrderDTOByStatus(EBillStatus.ORDERING);
        List<IOrderDTO> orderDTOList = getOrderDTOByStatusCooking();


        for (IOrderDTO item : orderDTOList) {
//            AppTable table = item.getTable().toTable();

//            List<OrderItemKitchenTableDTO> orderItemList = iOrderItemService.getOrderItemByStatusAndTable(orderItemStatus, table.getId());
            List<IOrderItemKitchenTableDTO> orderItemList = iOrderItemService.getOrderItemByStatusCookingAndTable(item.getTableId());

            if (orderItemList.size() != 0) {
//                int countProduct = this.countProductInOrder(orderItemList);
                int countProduct = this.countProductInOrderItem(orderItemList);

                OrderKitchenTableDTO orderKitchenDTO = new OrderKitchenTableDTO()
                        .setOrderId(item.getId())
                        .setTableId(item.getTableId())
                        .setTableName(item.getTableName())
                        .setCountProduct(countProduct)
                        .setUpdatedAt(item.getUpdatedAt())
                        .setOrderItems(orderItemList)
                ;

//                orderKitchenDTO.setOrderId(item.getId())
//                        .setTableId(item.getTableId())
//                        .setTableName(item.getName())
//                        .setCountProduct(countProduct)
//                        .setUpdatedAt(item.getUpdatedAt())
//                        .setOrderItems(orderItemList)
//                ;

                orderList.add(orderKitchenDTO);
            }
        }
        return orderList;
    }

    @Override
    public void softDelete(long orderId) {
        orderRepository.softDelete(orderId);
    }

    @Override
    public OrderAndListTableDTO createWithOrderItems(OrderWithOrderItemReqDTO orderWithOrderItemReqDTO, AppTable appTable) {

        Staff staff = iStaffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new DataInputException("Tên nhân viên không hợp lệ");
        });

        Optional<TableBackup> tableBackupOptional = iBackupTableService.findByTableTargetId(appTable.getId());

        OrderAndListTableDTO orderAndListTableDTO = new OrderAndListTableDTO();


        if(tableBackupOptional.isPresent()) {
            Optional<AppTable> tableCombineOptional = iTableService.findById(tableBackupOptional.get().getTableCurrentId());

            AppTable tableCombine = tableCombineOptional.get();

            tableCombine.setStatus(ETableStatus.BUSY);
            tableRepository.save(tableCombine);

            List<BillBackup> currentTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderCurrentId());

            if (currentTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu");
            }

            List<BillBackup> targetTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderTargetId());

            if (targetTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng muốn tách không hợp lệ, vui lòng kiểm tra lại dữ liệu");
            }

            orderAndListTableDTO.setTableCombine(tableCombine.toTableDTO());

            backupBillRepository.deleteAll(currentTableBillBackup);
            backupBillRepository.deleteAll(targetTableBillBackup);
            tableBackupRepository.delete(tableBackupOptional.get());
        }



        if (appTable.getStatus().equals(ETableStatus.EMPTY)) {
            appTable.setStatus(ETableStatus.BUSY);
            tableRepository.save(appTable);
        }

        Order order = (Order) new Order()
            .setId(null)
            .setTotalAmount(BigDecimal.ZERO)
            .setTable(appTable)
            .setStatus(EBillStatus.ORDERING)
            .setCreatedBy(staff.getUser().getUsername());

        orderRepository.save(order);

        List<OrderItemResDTO> orderItemResDTOS = new ArrayList<>();

        //lấy danh sách tất cả các orderItems được thêm vào
        List<OrderItemReqDTO> orderItemReqDTOS = orderWithOrderItemReqDTO.getItems();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // tính tổng tiền từng orderItem và tổng tiền tất cả món
        for (OrderItemReqDTO item : orderItemReqDTOS) {
            Long productId = item.getProductId();

            Product product = iProductService.findById(productId).orElseThrow(() -> {
                throw new DataInputException("ID sản phẩm không hợp lệ");
            });

            BigDecimal price = product.getPrice();
            int quantity = item.getQuantity();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
            OrderItem orderItem = item.toOrderItem(product, amount, EOrderItemStatus.COOKING, order, appTable);

            //xóa khoảng trắng đầu cuối và 2 khoảng trắng liên tiếp
            String note = orderItem.getNote();
            if (note != null && note.length() > 0) {
                note = orderItem.getNote().toLowerCase().trim().replaceAll("\\s{2,}", " ");
            }

            orderItem.setNote(note);

            if (!product.getCooking()) {
                orderItem.setStatus(EOrderItemStatus.WAITER);
            }

            orderItemRepository.save(orderItem);

            orderItemResDTOS.add(orderItem.toOrderItemResDTO());

            totalAmount = totalAmount.add(amount);
        }

        //set lại tổng tiền của order và trả ra.
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        List<OrderItemResDTO> newOrderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());




        orderAndListTableDTO.setOrderWithOrderItemResDTO(order.toOrderWithOrderItemResDTO(newOrderItemResDTOS));


        return orderAndListTableDTO;
    }

    @Override
    public OrderAndListTableDTO updateWithOrderItems(OrderWithOrderItemReqDTO orderWithOrderItemReqDTO, Order order) {

        Staff staff = iStaffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new DataInputException("Tên nhân viên không hợp lệ");
        });

        AppTable appTable = order.getTable();

        if(appTable.getStatus() == ETableStatus.COMBINE){
            appTable.setStatus(ETableStatus.BUSY);
            tableRepository.save(appTable);

            Optional<TableBackup> tableBackupOptional = iBackupTableService.findByTableCurrentId(appTable.getId());

            List<BillBackup> currentTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderCurrentId());

            if (currentTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu");
            }

            List<BillBackup> targetTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderTargetId());

            if (targetTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng muốn tách không hợp lệ, vui lòng kiểm tra lại dữ liệu");
            }

            backupBillRepository.deleteAll(currentTableBillBackup);
            backupBillRepository.deleteAll(targetTableBillBackup);
            tableBackupRepository.delete(tableBackupOptional.get());

        }

        //lấy danh sách các orderItem mới thêm vào.
        List<OrderItemReqDTO> orderItemListNew = orderWithOrderItemReqDTO.getItems();

        //chạy vòng lặp kiểm tra các orderItem mới có trùng với orderItem cũ hay không.
        for (OrderItemReqDTO orderItemReqDTO : orderItemListNew) {
            Product product = iProductService.findById(orderItemReqDTO.getProductId()).orElseThrow(() -> {
                throw new DataInputException("ID sản phẩm không hợp lệ");
            });

            //xóa khoảng trắng đầu cuối và 2 khoảng trắng liên tiếp
            String note = orderItemReqDTO.getNote();
            if (note != null && note.length() > 0) {
                note = orderItemReqDTO.getNote().toLowerCase().trim().replaceAll("\\s{2,}", " ");
            }

            EOrderItemStatus eOrderItemStatus;

            if (product.getCooking()) {
                eOrderItemStatus = EOrderItemStatus.COOKING;
            }
            else {
                eOrderItemStatus = EOrderItemStatus.WAITER;
            }

            orderItemReqDTO.setNote(note);
            Optional<OrderItem> orderItemOptional = orderItemRepository.findByOrderAndProductAndStatusAndNote(order, product, eOrderItemStatus, note);

            if (!orderItemOptional.isPresent()) {
                int quantity = orderItemReqDTO.getQuantity();
                BigDecimal price = product.getPrice();
                BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
                OrderItem orderItem = orderItemReqDTO.toOrderItem(product, amount, eOrderItemStatus, order, order.getTable());
                orderItem.setId(null);

                orderItem.setStatus(eOrderItemStatus);

                orderItemRepository.save(orderItem);
            }
            else {
                int currentQuantity = orderItemOptional.get().getQuantity();
                int newQuantity = currentQuantity + orderItemReqDTO.getQuantity();
                orderItemReqDTO.setQuantity(newQuantity);
                BigDecimal price = product.getPrice();
                BigDecimal amount = price.multiply(BigDecimal.valueOf(newQuantity));
                OrderItem orderItem = orderItemReqDTO.toOrderItem(product, amount, eOrderItemStatus, order, order.getTable());
                orderItem.setId(orderItemOptional.get().getId());

                orderItem.setStatus(eOrderItemStatus);

                orderItemRepository.save(orderItem);
            }
        }

        //tính lại tổng giá tiền của order
        BigDecimal newTotalAmount = calculateTotalAmount(order.getId());
        order.setTotalAmount(newTotalAmount);
        order.setUpdatedBy(staff.getUser().getUsername());
        orderRepository.save(order);

        List<OrderItemResDTO> orderItemResDTOS = iOrderItemService.getOrderItemResDTOByOrderId(order.getId());

        OrderAndListTableDTO orderAndListTableDTO = new OrderAndListTableDTO();
        orderAndListTableDTO.setOrderWithOrderItemResDTO(order.toOrderWithOrderItemResDTO(orderItemResDTOS));

        return orderAndListTableDTO;
    }

    @Override
    public BigDecimal calculateTotalAmount(Long orderId) {
        List<OrderItemDTO> orderItemList = iOrderItemService.getOrderItemDTOByOrderId(orderId);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDTO item : orderItemList) {
            BigDecimal price = item.getPrice();
            int quantity = item.getQuantity();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
            item.setAmount(amount);
            totalAmount = totalAmount.add(amount);
        }

        return totalAmount;
    }

}

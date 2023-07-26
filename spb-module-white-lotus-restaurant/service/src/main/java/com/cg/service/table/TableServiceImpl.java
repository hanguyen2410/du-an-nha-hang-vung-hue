package com.cg.service.table;

import com.cg.domain.dto.table.TableCountDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.User;
import com.cg.domain.entity.backup.BillBackup;
import com.cg.domain.entity.backup.TableBackup;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.*;
import com.cg.service.backupBill.IBackupBillService;
import com.cg.service.backupTable.IBackupTableService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class TableServiceImpl implements ITableService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private BackupBillRepository backupBillRepository;

    @Autowired
    private IBackupBillService iBackupBillService;

    @Autowired
    private TableBackupRepository tableBackupRepository;

    @Autowired
    private IBackupTableService iBackupTableService;


    @Override
    public List<AppTable> findAll() {
        return tableRepository.findAll();
    }

    @Override
    public List<TableDTO> getAllTableDTOWhereDeletedIsFalse() {
        return tableRepository.getAllTableDTOWhereDeletedIsFalse();
    }

    @Override
    public Page<TableDTO> findAllPageDeletedIsFalse(Pageable pageable) {
        return tableRepository.findAllPageDeletedIsFalse(pageable);
    }

    @Override
    public List<TableDTO> getTableDTOByStatusWhereDeletedIsFalse(ETableStatus status) {
        return tableRepository.getTableDTOByStatusWhereDeletedIsFalse(status);
    }

    @Override
    public AppTable getById(Long id) {
        return tableRepository.getById(id);
    }

    @Override
    public Optional<AppTable> findById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public void changeAllProductToNewTable(Long oldTableId, Long newTableId) {

        Optional<AppTable> oldTable = this.findById(oldTableId);

        if (!oldTable.isPresent()) {
            throw new DataInputException("bàn không tồn tại, vui lòng kiểm tra lại dữ liệu");
        }

        Optional<AppTable> newTable = this.findById(newTableId);

        if (!newTable.isPresent()) {
            throw new DataInputException("bàn không tồn tại, vui lòng kiểm tra lại dữ liệu");
        }

        Optional<TableBackup> tableBackupOptional = iBackupTableService.findByTableTargetId(newTable.get().getId());

        if(tableBackupOptional.isPresent()) {
            Optional<AppTable> tableCombineOptional = this.findById(tableBackupOptional.get().getTableCurrentId());

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

            backupBillRepository.deleteAll(currentTableBillBackup);
            backupBillRepository.deleteAll(targetTableBillBackup);
            tableBackupRepository.delete(tableBackupOptional.get());
        }

        Optional<Order> orderOptional = iOrderService.findByTableAndStatus(oldTable.get(), EBillStatus.ORDERING);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("đơn hàng không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        Order order = orderOptional.get();

        order.setTable(newTable.get());

        oldTable.get().setStatus(ETableStatus.EMPTY);
        newTable.get().setStatus(ETableStatus.BUSY);
    }

    @Override
    public void combineTable(AppTable currentTable, AppTable targetTable) {
        Optional<Order> orderCurrentOptional = iOrderService.findByTableAndStatus(currentTable, EBillStatus.ORDERING);

        if (!orderCurrentOptional.isPresent()) {
            throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        Optional<Order> orderTargetOptional = iOrderService.findByTableAndStatus(targetTable, EBillStatus.ORDERING);

        if (!orderTargetOptional.isPresent()) {
            throw new DataInputException("đơn hàng muốn gộp không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        List<OrderItem> orderItemCurrent = iOrderItemService.findAllByOrderId(orderCurrentOptional.get().getId());

        if (orderItemCurrent.size() == 0) {
            throw new DataInputException("Không tìm thấy hóa đơn của bàn hiện tại.");
        }

        List<OrderItem> orderItemTarget = iOrderItemService.findAllByOrderId(orderTargetOptional.get().getId());

        if (orderItemTarget.size() == 0) {
            throw new DataInputException("Không tìm thấy hóa đơn của bàn muốn gộp.");
        }

        TableBackup tableBackup = new TableBackup()
                .setOrderTargetId(orderCurrentOptional.get().getId())
                .setOrderCurrentId(orderTargetOptional.get().getId())
                .setTableCurrentId(targetTable.getId())
                .setTableTargetId(currentTable.getId());

        tableBackupRepository.save(tableBackup);

        for (OrderItem itemTarget : orderItemTarget) {
            backupBillRepository.save(itemTarget.toBillBackup(tableBackup));
        }

        List<OrderItem> newOrderItems = new ArrayList<>();

        for (OrderItem itemCurrent : orderItemCurrent) {
            boolean isExist = false;
            int itemIndex = 0;
            backupBillRepository.save(itemCurrent.toBillBackup(tableBackup));

            for (int i = 0; i < orderItemTarget.size(); i++) {
                if (Objects.equals(itemCurrent.getProduct().getId(), orderItemTarget.get(i).getProduct().getId())
                        && Objects.equals(itemCurrent.getNote(), orderItemTarget.get(i).getNote())
                        && itemCurrent.getStatus() == orderItemTarget.get(i).getStatus()
                ) {
                    isExist = true;
                    itemIndex = i;
                }
            }

            if (isExist) {
                int newQuantity = itemCurrent.getQuantity() + orderItemTarget.get(itemIndex).getQuantity();
                BigDecimal newAmount = itemCurrent.getPrice().multiply(BigDecimal.valueOf(newQuantity));

                orderItemTarget.get(itemIndex).setId(orderItemTarget.get(itemIndex).getId());
                orderItemTarget.get(itemIndex).setQuantity(newQuantity);
                orderItemTarget.get(itemIndex).setAmount(newAmount);
                orderItemRepository.save(orderItemTarget.get(itemIndex));
            } else {
                itemCurrent.setOrder(orderItemTarget.get(itemIndex).getOrder());
                itemCurrent.setTable(orderItemTarget.get(itemIndex).getTable());
                OrderItem newOrderItem = new OrderItem().initValue(itemCurrent);
                newOrderItem.setId(null);
                newOrderItems.add(newOrderItem);
            }

            isExist = false;
            itemIndex = 0;
        }

        currentTable.setStatus(ETableStatus.EMPTY);
        tableRepository.save(currentTable);

        targetTable.setStatus(ETableStatus.COMBINE);
        tableRepository.save(targetTable);


        BigDecimal newTotalAmount = iOrderService.calculateTotalAmount(orderTargetOptional.get().getId());
        orderTargetOptional.get().setTotalAmount(newTotalAmount);
        orderRepository.save(orderTargetOptional.get());

        orderItemRepository.deleteAll(orderItemCurrent);

        orderRepository.delete(orderCurrentOptional.get());

        orderItemRepository.saveAll(newOrderItems);
    }

    @Override
    public AppTable unCombineTable(AppTable currentTable) {

        Optional<Order> orderCurrentOptional = iOrderService.findByTableAndStatus(currentTable, EBillStatus.ORDERING);

        if (!orderCurrentOptional.isPresent()) {
            throw new DataInputException("Đơn hàng hiện tại không có, vui lòng kiểm tra lại dữ liệu");
        }

        List<OrderItem> orderItemCurrent = iOrderItemService.findAllByOrderId(orderCurrentOptional.get().getId());

        Optional<TableBackup> tableBackupOptional = iBackupTableService.findByTableCurrentIdAndOrderCurrentId(currentTable.getId(), orderCurrentOptional.get().getId());

        if (!tableBackupOptional.isPresent()) {
            throw new DataInputException("bàn hiện tại không thể tách, vui lòng kiểm tra lại.");
        }

        Optional<AppTable> tableTargetOptional = tableRepository.findById(tableBackupOptional.get().getTableTargetId());

        if (!tableTargetOptional.isPresent()) {
            throw new DataInputException("Bàn muốn tách không tồn tại");
        }

        Optional<Order> orderTableTargetOptional = orderRepository.findByTableAndStatus(tableTargetOptional.get(), EBillStatus.ORDERING);

        if (orderTableTargetOptional.isPresent()) {
            throw new DataInputException("Bàn muốn tách đã được sử dụng không còn trống");
        }

        List<BillBackup> currentTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderCurrentId());

        if (currentTableBillBackup.size() == 0) {
            throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        List<BillBackup> targetTableBillBackup = iBackupBillService.findAllByOrderId(tableBackupOptional.get().getOrderTargetId());

        if (targetTableBillBackup.size() == 0) {
            throw new DataInputException("đơn hàng muốn tách không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        orderItemRepository.deleteAll(orderItemCurrent);
        orderRepository.delete(orderCurrentOptional.get());

        Order orderCurrent = new Order();
        AppTable appTableCurrent = tableRepository.findById(tableBackupOptional.get().getTableCurrentId()).get();
        orderCurrent.setTable(appTableCurrent);
        orderCurrent.setStatus(EBillStatus.ORDERING);
        orderCurrent.setTotalAmount(BigDecimal.ZERO);
        orderRepository.save(orderCurrent);

        for (BillBackup itemCurrentBackup : currentTableBillBackup) {
            Product product = productRepository.findById(itemCurrentBackup.getProductId()).get();
            OrderItem backupCurrentOrderItem = new OrderItem().backupValue(itemCurrentBackup, orderCurrent, appTableCurrent, product);
            orderItemRepository.save(backupCurrentOrderItem);
        }

        Order orderTarget = new Order();
        AppTable appTableTarget = tableRepository.findById(tableBackupOptional.get().getTableTargetId()).get();
        orderTarget.setTable(appTableTarget);
        orderTarget.setStatus(EBillStatus.ORDERING);
        orderTarget.setTotalAmount(BigDecimal.ZERO);
        orderRepository.save(orderTarget);

        for (BillBackup itemTargetBackup : targetTableBillBackup) {
            Product product = productRepository.findById(itemTargetBackup.getProductId()).get();
            OrderItem backupTargetOrderItem = new OrderItem().backupValue(itemTargetBackup, orderTarget, appTableTarget, product);
            orderItemRepository.save(backupTargetOrderItem);
        }

        Optional<AppTable> targetTableOptional = tableRepository.findById(tableBackupOptional.get().getTableTargetId());

        targetTableOptional.get().setStatus(ETableStatus.BUSY);

        currentTable.setStatus(ETableStatus.BUSY);

        tableRepository.save(targetTableOptional.get());
        tableRepository.save(currentTable);

        backupBillRepository.deleteAll(currentTableBillBackup);
        backupBillRepository.deleteAll(targetTableBillBackup);
        tableBackupRepository.delete(tableBackupOptional.get());

        return targetTableOptional.get();
    }

    @Override
    public void saveTable(AppTable appTable) {
        tableRepository.save(appTable);

        String username = appUtils.getPrincipalUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new DataInputException("User invalid");
        });

        Staff staff = staffRepository.findByUser(user).orElseThrow(() -> {
            throw new DataInputException("Staff invalid");
        });

//        Order order = new Order();
//        order.setTable(appTable);
//        order.setTotalAmount(BigDecimal.ZERO);
//        orderRepository.save(order);
//
//        Bill bill = new Bill();
//        bill.setTable(appTable);
//        bill.setChargeMoney(BigDecimal.ZERO);
//        bill.setChargePercent(0L);
//        bill.setDiscountMoney(BigDecimal.ZERO);
//        bill.setDiscountPercent(0L);
//        bill.setStaff(staff);
//        bill.setTotalAmount(BigDecimal.ZERO);
//        bill.setOrder(order);
//        bill.setStatus(EBillStatus.ORDERING);
//        bill.setOrderPrice(order.getTotalAmount());
//        billRepository.save(bill);
    }

    @Override
    public AppTable closeTable(AppTable appTable) {
        Optional<Order> orderOptional = iOrderService.findByTableAndStatus(appTable, EBillStatus.ORDERING);

        if(!orderOptional.isPresent()){
            throw new DataInputException("Hóa đơn bàn ăn không hợp lệ.");
        }

        List<OrderItem> orderItemList = iOrderItemService.findAllByOrderId(orderOptional.get().getId());

        if(orderItemList.size() > 0){
            throw new DataInputException("Bàn đang còn món ăn nên không thể đóng bàn.");
        }

        iOrderService.deleteById(orderOptional.get().getId());
        appTable.setStatus(ETableStatus.EMPTY);
        tableRepository.save(appTable);

        return appTable;
    }

    @Override
    public AppTable save(AppTable appTable) {
        return tableRepository.save(appTable);
    }

    @Override
    public void delete(AppTable appTable) {
        appTable.setDeleted(true);
        tableRepository.save(appTable);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public TableCountDTO countTable() {
        return tableRepository.countTable();
    }
}

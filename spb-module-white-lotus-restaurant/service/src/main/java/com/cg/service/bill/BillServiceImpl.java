package com.cg.service.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.report.*;
import com.cg.domain.entity.backup.BillBackup;
import com.cg.domain.entity.backup.TableBackup;
import com.cg.domain.entity.bill.Bill;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.EOrderItemStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.exception.UnauthorizedException;
import com.cg.repository.*;
import com.cg.service.backupBill.IBackupBillService;
import com.cg.service.backupTable.BackupTableServiceImpl;
import com.cg.service.backupTable.IBackupTableService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class BillServiceImpl implements IBillService {

    @Autowired
    IBillService iBillService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private IStaffService iStaffService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private IBackupTableService iBackupTableService;

    @Autowired
    private IBackupBillService iBackupBillService;

    @Autowired
    private BackupBillRepository backupBillRepository;

    @Autowired
    private TableBackupRepository tableBackupRepository;

    @Autowired
    private ITableService iTableService;

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public Bill getById(Long aLong) {
        return null;
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void delete(Bill bill) {
        billRepository.delete(bill);
    }

    @Override
    public void deleteById(Long id) {
        billRepository.deleteById(id);
    }

    @Override
    public List<Bill> findALlByOrderIdAndStatus(Long orderId, EBillStatus eBillStatus) {
        return billRepository.findALlByOrderIdAndStatus(orderId, eBillStatus);
    }

    @Override
    public BillPrintTempDTO print(Order order) {

        Bill bill = iBillService.findBillByOrderId(order.getId()).orElseThrow(() -> {
            throw new DataInputException("ID hóa đơn không tồn tại.");
        });

        List<BillPrintItemDTO> items = orderItemRepository.getAllBillPrintItemDTOByOrderId(order.getId());

        return bill.toBillPrintTempDTO(items, order.getCreatedAt());
    }

    @Override
    public void pay(Long orderId, Long chargePercent, BigDecimal chargeMoney, Long discountPercent, BigDecimal discountMoney, BigDecimal totalAmount, BigDecimal transferPay, BigDecimal cashPay) {

        Optional<TableBackup> tableBackupOptional = iBackupTableService.findByOrderCurrentId(orderId);

        if(tableBackupOptional.isPresent()) {

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



        Staff staff = iStaffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new UnauthorizedException("Vui lòng xác thực");
        });

        Order order = iOrderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("ID order không tồn tại.");
        });

        order.setStatus(EBillStatus.PAID);
        order = orderRepository.save(order);

//        List<OrderItemDTO> orderItemList = iOrderItemService.getOrderItemDTOByOrderId(order.getId());
//
//        for (OrderItemDTO item : orderItemList) {
//            item.setStatus(EOrderItemStatus.DONE);
//            orderItemRepository.save(item.toOrderItem());
//        }

        List<OrderItem> orderItems = iOrderItemService.getAllByOrder(order);
        orderItems.forEach(item -> item.setStatus(EOrderItemStatus.DONE));

        orderItemRepository.saveAll(orderItems);

        Bill bill = new Bill()
                .setId(null)
                .setOrder(order)
                .setStaff(staff)
                .setTable(order.getTable())
                .setOrderPrice(order.getTotalAmount())
                .setChargePercent(chargePercent)
                .setChargeMoney(chargeMoney)
                .setDiscountPercent(discountPercent)
                .setDiscountMoney(discountMoney)
                .setTotalAmount(totalAmount)
                .setTransferPay(transferPay)
                .setCashPay(cashPay)
                .setStatus(EBillStatus.PAID);
//        bill = bill.initBillPrePay(order, staff);
        billRepository.save(bill);

//        Bill bill = iBillService.getBillByOrderId(order.getId()).orElseThrow(() -> {
//            throw new DataInputException("ID hóa đơn không tồn tại.");
//        });

        AppTable appTable = order.getTable();
        appTable.setStatus(ETableStatus.EMPTY);
        tableRepository.save(appTable);
    }

    //    @Override
//    public List<BillCreateDTO> getBillCreateDTOByBillId(Long billId) {
//        return billRepository.getBillCreateDTOByBillId(billId);
//    }
//
    @Override
    public Optional<Bill> findBillByOrderId(Long orderId) {
        return billRepository.findBillByOrderId(orderId);
    }

    @Override
    public Optional<Bill> findByOrderAndStatus(Order order, EBillStatus eBillStatus) {
        return billRepository.findByOrderAndStatus(order, eBillStatus);
    }

    @Override
    public BillResDTO createBillWithOrders(BillCreateDTO billCreateDTO) {

        Staff staff = iStaffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new DataInputException("Tên nhân viên không hợp lệ");
        });

        Order order = iOrderService.findById(billCreateDTO.getOrderId()).orElseThrow(() -> new DataInputException("ID Hóa đơn không hợp lệ."));


        List<OrderItemDTO> orderItemList = iOrderItemService.getOrderItemDTOByOrderId(order.getId());

        for (OrderItemDTO item : orderItemList) {
            if (item.toOrderItem().getStatus() == EOrderItemStatus.WAITER) {
                throw new DataInputException("Hiện tại vẫn còn sản phẩm đang chờ, Không thể thanh toán!");
            }
        }

        Long discountPercent = billCreateDTO.getDiscountPercent();

        Long chargePercent = billCreateDTO.getChargePercent();

        if (discountPercent == 0 && chargePercent == 0) {
            Bill bill = new Bill()
                    .setOrder(order)
                    .setStaff(staff)
                    .setTable(order.getTable())
                    .setDiscountPercent(discountPercent)
                    .setDiscountMoney(BigDecimal.ZERO)
                    .setChargePercent(chargePercent)
                    .setChargeMoney(BigDecimal.ZERO)
                    .setOrderPrice(order.getTotalAmount())
                    .setTotalAmount(order.getTotalAmount())
                    .setStatus(EBillStatus.ORDERING);
            billRepository.save(bill);
            return bill.toBillResDTO();
        } else {
            if (chargePercent == 0) {

                BigDecimal discountMoney = order.getTotalAmount().multiply(BigDecimal.valueOf(billCreateDTO.getDiscountPercent())).divide(BigDecimal.valueOf(100L));
                BigDecimal totalAmountBill = order.getTotalAmount().subtract(discountMoney);
                Bill bill = new Bill()
                        .setOrder(order)
                        .setStaff(staff)
                        .setTable(order.getTable())
                        .setDiscountPercent(billCreateDTO.getDiscountPercent())
                        .setDiscountMoney(discountMoney)
                        .setChargePercent(chargePercent)
                        .setChargeMoney(BigDecimal.ZERO)
                        .setOrderPrice(order.getTotalAmount())
                        .setTotalAmount(totalAmountBill)
                        .setStatus(EBillStatus.ORDERING);
                billRepository.save(bill);
                return bill.toBillResDTO();
            } else if (discountPercent == 0) {
                BigDecimal chargeMoney = order.getTotalAmount().multiply(BigDecimal.valueOf(billCreateDTO.getChargePercent())).divide(BigDecimal.valueOf(100L));
                BigDecimal totalAmountBill = order.getTotalAmount().add(chargeMoney);
                Bill bill = new Bill()
                        .setOrder(order)
                        .setStaff(staff)
                        .setTable(order.getTable())
                        .setDiscountPercent(discountPercent)
                        .setDiscountMoney(BigDecimal.ZERO)
                        .setChargePercent(chargePercent)
                        .setChargeMoney(chargeMoney)
                        .setOrderPrice(order.getTotalAmount())
                        .setTotalAmount(totalAmountBill)
                        .setStatus(EBillStatus.ORDERING);
                billRepository.save(bill);
                return bill.toBillResDTO();
            } else {
                BigDecimal chargeMoney = order.getTotalAmount().multiply(BigDecimal.valueOf(chargePercent)).divide(BigDecimal.valueOf(100L));
                BigDecimal discountMoney = order.getTotalAmount().multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100L));
                BigDecimal totalAmountBill = order.getTotalAmount().add(chargeMoney).subtract(discountMoney);
                Bill bill = new Bill()
                        .setOrder(order)
                        .setStaff(staff)
                        .setTable(order.getTable())
                        .setDiscountPercent(discountPercent)
                        .setDiscountMoney(discountMoney)
                        .setChargePercent(chargePercent)
                        .setChargeMoney(chargeMoney)
                        .setOrderPrice(order.getTotalAmount())
                        .setTotalAmount(totalAmountBill)
                        .setStatus(EBillStatus.ORDERING);
                billRepository.save(bill);
                return bill.toBillResDTO();
            }
        }
    }

    @Override
    public List<YearReportDTO> getReportByYear(int year) {
        return billRepository.getReportByYear(year);
    }

    @Override
    public List<IYearReportDTO> getReportByCurrentYear() {
        return billRepository.getReportByCurrentYear();
    }

    @Override
    public List<I6MonthAgoReportDTO> getReport6MonthAgo() {
        return billRepository.getReport6MonthAgo();
    }

    @Override
    public YearReportDTO getReportByMonth(int month, int year) {
        return billRepository.getReportByMonth(month, year);
    }

//    @Override
//    public List<ReportDTO> getReportOfCurrentMonth() {
//        return billRepository.getReportOfCurrentMonth();
//    }

    @Override
    public IReportDTO getReportOfCurrentDay() {
        return billRepository.getReportOfCurrentDay();
    }

    @Override
    public IReportDTO getReportOfCurrentMonth() {
        return billRepository.getReportOfCurrentMonth();
    }

    @Override
    public ReportDTO getReportOfDay(String day) {
        return billRepository.getReportOfDay(day);
    }

    @Override
    public List<IDayToDayReportDTO> getReportFrom10DaysAgo() {
        return billRepository.getReportFrom10DaysAgo();
    }

    @Override
    public List<DayToDayReportDTO> getReportFromDayToDay(String startDay, String endDay) {
        return billRepository.getReportFromDayToDay(startDay, endDay);
    }

    @Override
    public List<BillOfTheDayDTO> countBillCurrentDay(String day) {
        return billRepository.countBillCurrentDay(day);
    }

    @Override
    public Page<BillGetAllResDTO> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable) {
        return billRepository.findAll(billFilterReqDTO, pageable).map(Bill::toBillGetAllResDTO);
    }

    @Override
    public PayDTO payOfDay() {
        return billRepository.payOfDay();
    }

    public Page<BillGetAllResDTO> getBillsByDay(String day, Pageable pageable) {
        return billRepository.getBillsByDay(day, pageable);
    }

    @Override
    public List<BillGetTwoDayDTO> getBillsNotPaging(String day) {
        return billRepository.getBillsNotPaging(day);
    }
}

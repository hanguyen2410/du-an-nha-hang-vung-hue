package com.cg.service.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.report.*;
import com.cg.domain.entity.bill.Bill;

import com.cg.domain.entity.order.Order;
import com.cg.domain.enums.EBillStatus;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IBillService extends IGeneralService<Bill, Long> {

    BillPrintTempDTO print(Order order);

    void pay(Long orderId, Long chargePercent, BigDecimal chargeMoney, Long discountPercent, BigDecimal discountMoney, BigDecimal totalAmount, BigDecimal transferPay, BigDecimal cashPay);

//    List<BillCreateDTO> getBillCreateDTOByBillId(Long billId);

    Optional<Bill> findBillByOrderId(Long orderId);

    Optional<Bill> findByOrderAndStatus(Order order, EBillStatus eBillStatus);

    List<Bill> findALlByOrderIdAndStatus(Long orderId, EBillStatus eBillStatus);

    List<YearReportDTO> getReportByYear(int year);

    List<IYearReportDTO> getReportByCurrentYear();

    List<I6MonthAgoReportDTO> getReport6MonthAgo();

    YearReportDTO getReportByMonth(int month, int year);

//    List<ReportDTO> getReportOfCurrentMonth();

    IReportDTO getReportOfCurrentDay();

    IReportDTO getReportOfCurrentMonth();

    ReportDTO getReportOfDay(String day);

    List<IDayToDayReportDTO> getReportFrom10DaysAgo();

    List<DayToDayReportDTO> getReportFromDayToDay(String startDay, String endDay);

    List<BillOfTheDayDTO> countBillCurrentDay(String day);

    Page<BillGetAllResDTO> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable);

    BillResDTO createBillWithOrders(BillCreateDTO billCreateDTO);

    PayDTO payOfDay();

    Page<BillGetAllResDTO> getBillsByDay(String day, Pageable pageable);

    List<BillGetTwoDayDTO> getBillsNotPaging(String day);

}

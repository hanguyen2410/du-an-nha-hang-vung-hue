package com.cg.repository;

import com.cg.domain.dto.bill.BillFilterReqDTO;
import com.cg.domain.dto.bill.BillGetAllResDTO;
import com.cg.domain.dto.bill.BillGetTwoDayDTO;


import com.cg.domain.dto.report.*;
import com.cg.domain.entity.bill.Bill;

import com.cg.domain.entity.order.Order;
import com.cg.domain.enums.EBillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.*;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

//    @Query("SELECT NEW com.cg.domain.dto.bill.BillCreateDTO (" +
//                "bi.orderPrice, " +
//                "bi.discountMoney, " +
//                "bi.discountPercent, " +
//                "bi.chargeMoney," +
//                "bi.chargePercent, " +
//                "bi.totalAmount, " +
//                "bi.table.id, " +
//                "bi.order, " +
//                "bi.status " +
//            ") " +
//            "FROM Bill AS bi " +
//            "WHERE bi.deleted = false " +
//            "AND bi.order.id = :billId "
//    )
//    List<BillCreateDTO> getBillCreateDTOByBillId(@Param("billId") Long billId);

    @Query("SELECT NEW com.cg.domain.entity.bill.Bill (" +
                "bi.id, " +
                "bi.orderPrice, " +
                "bi.discountMoney, " +
                "bi.discountPercent, " +
                "bi.chargeMoney," +
                "bi.chargePercent, " +
                "bi.totalAmount, " +
                "bi.table," +
                "bi.staff, " +
                "bi.order, " +
                "bi.status, " +
                "bi.cashPay, " +
                "bi.transferPay " +
            ") " +
            "FROM Bill AS bi " +
            "WHERE bi.deleted = false " +
            "AND bi.order.id = :orderId "
    )
    Optional<Bill> findBillByOrderId(@Param("orderId") Long orderId);


    Optional<Bill> findByOrderAndStatus(Order order, EBillStatus status);


    List<Bill> findALlByOrderIdAndStatus(Long orderId, EBillStatus eBillStatus);
    @Query("SELECT NEW com.cg.domain.dto.report.YearReportDTO (" +
                "MONTH(bill.createdAt), " +
                "SUM(bill.totalAmount), " +
                "COUNT(bill.id) " +
            ") " +
            "FROM Bill AS bill " +
            "WHERE MONTH(bill.createdAt) = :month " +
            "AND YEAR(bill.createdAt) = :year " +
            "AND bill.status = 'PAID' " +
            "GROUP BY MONTH(bill.createdAt)"
    )
    YearReportDTO getReportByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT NEW com.cg.domain.dto.report.YearReportDTO (" +
                "MONTH(bill.createdAt), " +
                "SUM(bill.totalAmount), " +
                "COUNT(bill.id) " +
            ") " +
            "FROM Bill AS bill " +
            "WHERE YEAR(bill.createdAt) = :year " +
            "AND bill.status = 'PAID' " +
            "GROUP BY MONTH(bill.createdAt) " +
            "ORDER BY MONTH(bill.createdAt) ASC"
    )
    List<YearReportDTO> getReportByYear(@Param("year") int year);


    @Query(value = "SELECT * FROM vw_get_report_of_current_year", nativeQuery = true)
    List<IYearReportDTO> getReportByCurrentYear();

    @Query(value = "SELECT * FROM vw_get_report_6_month_ago", nativeQuery = true)
    List<I6MonthAgoReportDTO> getReport6MonthAgo();

    @Query("SELECT NEW com.cg.domain.dto.report.ReportDTO (" +
                "SUM(bill.totalAmount), " +
                "COUNT(bill.id) " +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') = :day " +
            "AND bill.status = 'PAID' "
    )
    ReportDTO getReportOfDay(@Param("day") String day);


    @Query(value = "SELECT * FROM vw_get_report_of_current_day", nativeQuery = true)
    IReportDTO getReportOfCurrentDay();

    @Query(value = "SELECT * FROM vw_get_report_of_current_month", nativeQuery = true)
    IReportDTO getReportOfCurrentMonth();

    @Query("SELECT NEW com.cg.domain.dto.report.DayToDayReportDTO (" +
                "DATE_FORMAT(bill.createdAt,'%d/%m/%Y'), " +
                "SUM(bill.totalAmount) " +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') > :startDay " +
            "AND DATE_FORMAT(bill.createdAt,'%Y-%m-%d') < :endDay " +
            "AND bill.status = 'PAID' " +
            "GROUP BY DATE_FORMAT(bill.createdAt,'%d/%m/%Y') " +
            "ORDER BY DATE_FORMAT(bill.createdAt,'%d/%m/%Y')"
    )
    List<DayToDayReportDTO> getReportFromDayToDay(@Param("startDay") String startDay,
                                                  @Param("endDay") String endDay);


    @Query(value = "SELECT * FROM vw_get_report_from_10_days_ago", nativeQuery = true)
    List<IDayToDayReportDTO> getReportFrom10DaysAgo();

//    @Query("SELECT NEW com.cg.domain.dto.report.ReportDTO (" +
//            "sum(bill.totalAmount) " +
//            ") " +
//            "FROM Bill AS bill " +
//            "WHERE MONTH(Date_Format(bill.createdAt,'%Y/%m/%d')) = MONTH(CURRENT_DATE()) " +
//            "AND YEAR(Date_Format(bill.createdAt,'%Y/%m/%d')) = YEAR(CURRENT_DATE()) " +
//            "AND bill.status = 'PAID'"
//    )
//    List<ReportDTO> getReportOfCurrentMonth();

    @Query("SELECT NEW com.cg.domain.dto.report.BillOfTheDayDTO (" +
                "COUNT(bill.id) " +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') = :day " +
            "AND bill.status = 'PAID' "
    )
    List<BillOfTheDayDTO> countBillCurrentDay(@Param("day") String day);


    default Page<Bill> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String status = billFilterReqDTO.getStatus();

            if (status != null && !status.equals("ALL")) {
                Predicate predicate = criteriaBuilder.equal(root.get("status"), EBillStatus.valueOf(status));
                predicates.add(predicate);
            }

            Date billFrom = billFilterReqDTO.getBillFrom();
            Date billTo = billFilterReqDTO.getBillTo();

            if (billFrom != null && billTo != null) {
                Predicate predicate = criteriaBuilder.between(root.get("createdAt"), billFrom, billTo);
                predicates.add(predicate);
            }
            else {
                if (billFrom != null) {
                    Predicate predicate = criteriaBuilder.greaterThan(root.get("createdAt"), billFrom);
                    predicates.add(predicate);
                }

                if (billTo != null) {
                    Predicate predicate = criteriaBuilder.lessThan(root.get("createdAt"), billTo);
                    predicates.add(predicate);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }

    @Query("SELECT NEW com.cg.domain.dto.report.PayDTO (" +
                "SUM(bill.cashPay), " +
                "SUM(bill.transferPay)" +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') = CURRENT_DATE " +
            "AND bill.status = 'PAID' "
    )
    PayDTO payOfDay();


    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetAllResDTO (" +
                "bill.id, " +
                "bill.createdAt, " +
                "bill.orderPrice, " +
                "bill.discountMoney, " +
                "bill.discountPercent, " +
                "bill.chargeMoney, " +
                "bill.chargePercent, " +
                "bill.totalAmount, " +
                "bill.table.id, " +
                "bill.table.name, " +
                "bill.order.id, " +
                "bill.staff.id, " +
                "bill.staff.fullName, " +
                "bill.status"  +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') = :day " +
            "AND bill.status = 'PAID' "
    )
    Page<BillGetAllResDTO> getBillsByDay(@Param("day") String day, Pageable pageable);

    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetTwoDayDTO (" +
                "bill.id, " +
                "bill.createdAt, " +
                "bill.orderPrice, " +
                "bill.discountMoney, " +
                "bill.discountPercent, " +
                "bill.chargeMoney, " +
                "bill.chargePercent, " +
                "bill.totalAmount, " +
                "bill.table.id, " +
                "bill.table.name, " +
                "bill.order.id, " +
                "bill.staff.id, " +
                "bill.staff.fullName, " +
                "bill.status"  +
            ") " +
            "FROM Bill AS bill " +
            "WHERE DATE_FORMAT(bill.createdAt,'%Y-%m-%d') = :day " +
            "AND bill.status = 'PAID' "
    )
    List<BillGetTwoDayDTO> getBillsNotPaging(@Param("day") String day);
}

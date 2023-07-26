package com.cg.api;

import com.cg.domain.dto.product.IProductReportDTO;
import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.report.*;
import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.table.TableCountDTO;
import com.cg.exception.DataInputException;
import com.cg.service.bill.IBillService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportAPI {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IBillService billService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private ITableService tableService;


    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        IReportDTO dayReport = billService.getReportOfCurrentDay();
        IReportDTO monthReport = billService.getReportOfCurrentMonth();
        List<IYearReportDTO> yearReport = billService.getReportByCurrentYear();
        List<I6MonthAgoReportDTO> sixMonthAgoReport = billService.getReport6MonthAgo();
        List<IProductReportDTO> top5BestSellReport = orderItemService.getTop5BestSellCurrentMonth();
        List<IDayToDayReportDTO> tenDaysAgoReport = billService.getReportFrom10DaysAgo();
        ProductCountDTO  countProduct =  productService.countProduct();
        StaffCountDTO countStaff = staffService.countStaff();
        TableCountDTO countTable = tableService.countTable();

        AllReportDTO allReportDTO = new AllReportDTO();
        allReportDTO.setDayReport(dayReport);
        allReportDTO.setMonthReport(monthReport);
        allReportDTO.setYearReport(yearReport);
        allReportDTO.setTop5BestSellReport(top5BestSellReport);
        allReportDTO.setTenDaysAgoReport(tenDaysAgoReport);
        allReportDTO.setCountProduct(countProduct.getCountProduct());
        allReportDTO.setCountStaff(countStaff.getCountStaff());
        allReportDTO.setCountTable(countTable.getCountTable());
        allReportDTO.setSixMonthAgoReport(sixMonthAgoReport);


        return new ResponseEntity<>(allReportDTO, HttpStatus.OK);
    }

    @GetMapping("/day/{day}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CASHIER')")
    public ResponseEntity<?> getReportOfDay(@PathVariable String day) {

        ReportDTO report = billService.getReportOfDay(day);

        if (report == null) {
            throw new DataInputException("Ngày " + day + " chưa có doanh thu!");
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/current-day")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportOfCurrentDay() {

        IReportDTO report = billService.getReportOfCurrentDay();

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/current-month")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportOfCurrentMonth() {

        IReportDTO report = billService.getReportOfCurrentMonth();

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/current-year")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportOfCurrentYear() {

        List<IYearReportDTO> report = billService.getReportByCurrentYear();

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportByYear(@PathVariable int year) {

        List<YearReportDTO> reportMonth = billService.getReportByYear(year);

        if (reportMonth.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth, HttpStatus.OK);
    }

    @GetMapping("/month/{year}-{month}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportByMonth(@PathVariable int year, @PathVariable int month) {

        YearReportDTO reportMonth = billService.getReportByMonth(month, year);

        if (reportMonth == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth, HttpStatus.OK);
    }

//    @GetMapping("/month/current-month")
////    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
//    public ResponseEntity<?> getReportOfCurrentMonth() {
//
//        List<ReportDTO> reportMonth = billService.getReportOfCurrentMonth();
//
//        if (reportMonth.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(reportMonth.get(0).getTotalAmount(), HttpStatus.OK);
//    }

    @GetMapping("/10-days-ago")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportFrom10DaysAgo() {
        List<IDayToDayReportDTO> list = billService.getReportFrom10DaysAgo();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/day/{startDay}/{endDay}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getReportFromDayToDay(@PathVariable String startDay, @PathVariable String endDay) {

        String[] startDayArray = startDay.split("-");
        String[] endDayArray = endDay.split("-");

        int startDayTemp = Integer.parseInt(startDayArray[startDayArray.length - 1]) - 1;
        if (startDayTemp < 10)
            startDayArray[startDayArray.length - 1] = "0" + startDayTemp;
        else
            startDayArray[startDayArray.length - 1] = String.valueOf(startDayTemp);

        startDay = String.join("-", startDayArray);

        int endDayTemp = Integer.parseInt(endDayArray[endDayArray.length - 1]) + 1;
        if (endDayTemp < 10)
            endDayArray[endDayArray.length - 1] = "0" + endDayTemp;
        else
            endDayArray[endDayArray.length - 1] = String.valueOf(endDayTemp);

        endDay = String.join("-", endDayArray);

        List<DayToDayReportDTO> report = billService.getReportFromDayToDay(startDay, endDay);

        if (report.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/product/top5-best-sell")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getTop5ProductBestSellCurrentMonth() {
        List<IProductReportDTO> reportProductDTOS = orderItemService.getTop5BestSellCurrentMonth();

        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }

    @GetMapping("/product/top5-unmarket-table")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getTop5ProductUnMarketTableCurrentMonth() {
        List<IProductReportDTO> reportProductDTOS = orderItemService.getTop5ProductUnMarketTableCurrentMonth();

        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }

    @GetMapping("/product/top5/{year}-{month}-{sort}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getTop5Product(@PathVariable int year, @PathVariable int month, Pageable pageable, @PathVariable String sort) {
        List<ProductReportDTO> reportProductDTOS = new ArrayList<ProductReportDTO>();

        if (sort.equalsIgnoreCase("ASC")){
            reportProductDTOS = orderItemService.getTop5ProductUnmarketable(month, year, pageable );
        }
        else if (sort.equalsIgnoreCase("DESC")) {
            reportProductDTOS = orderItemService.getTop5ProductBestSell(month, year, pageable );
        }

        if (reportProductDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }
    @GetMapping("/count-bill-day/{day}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> countBillOfCurrentDay(@PathVariable String day) {
        return new ResponseEntity<>(billService.countBillCurrentDay(day).get(0).getCount(), HttpStatus.OK);
    }

    @GetMapping("/pay-day")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CASHIER')")
    public ResponseEntity<?> payOfDay( ){
        return new ResponseEntity<>(billService.payOfDay(), HttpStatus.OK);
    }

}

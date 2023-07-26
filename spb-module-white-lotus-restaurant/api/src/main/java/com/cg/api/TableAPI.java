package com.cg.api;

import com.cg.domain.dto.table.TableCreateDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tables")
public class TableAPI {
    @Autowired
    private ITableService tableService;

    @Autowired
    private ValidateUtils validateUtils;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private AppUtils appUtils;

//    @GetMapping()
//    public ResponseEntity<?> getAllTables(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
//        Page<TableDTO> tableDTOS = tableService.findAllPageDeletedIsFalse(pageable);
//
//        if(tableDTOS.getTotalElements() == 0){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(tableDTOS, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<?> getAllTable() {
        List<TableDTO> tableDTOList = tableService.getAllTableDTOWhereDeletedIsFalse();
        if (tableDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tableDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getTableById(@PathVariable Long id) {
        Optional<AppTable> optionalAppTable = tableService.findById(id);

        if (!optionalAppTable.isPresent()) {
            throw new DataInputException("ID không hợp lệ !");
        }

        return new ResponseEntity<>(optionalAppTable.get().toTableDTO(), HttpStatus.OK);
    }

    @GetMapping("/status={status}")
    public ResponseEntity<?> getTableDTOByStatus(@PathVariable String status) {
        List<TableDTO> tableDTOList = tableService.getTableDTOByStatusWhereDeletedIsFalse(ETableStatus.valueOf(status.toUpperCase()));

        if (tableDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tableDTOList, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody TableCreateDTO tableCreateDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        AppTable appTable = new AppTable();
        appTable.setName(tableCreateDTO.getName());

        appTable.setStatus(ETableStatus.EMPTY);
        tableService.save(appTable);

        return new ResponseEntity<>(appTable.toTableDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        AppTable appTable = tableService.findById(id).orElseThrow(() -> {
            throw new DataInputException("Bàn không hợp lệ");
        });

        if (appTable.getStatus().equals(ETableStatus.BOOKED)) {
            throw new DataInputException("Bàn không hợp lệ");
        } else if (appTable.getStatus().equals(ETableStatus.BUSY)) {
            appTable.setStatus(ETableStatus.EMPTY);
        } else {
            appTable.setStatus(ETableStatus.BUSY);
        }

        tableService.save(appTable);

        return new ResponseEntity<>(appTable.toTableDTO(), HttpStatus.OK);
    }

    @PostMapping("/change-table")
    public ResponseEntity<?> changeAllProductToNewTable(HttpServletRequest request) throws IOException {


        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String oldTableIdStr;
        String newTableIdStr;

        try {
            oldTableIdStr = json.get("oldTableId").asText();
            newTableIdStr = json.get("newTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(oldTableIdStr)) {
            throw new DataInputException("ID bàn cũ phải là ký tự số");
        }

        if (!validateUtils.isNumberValid(newTableIdStr)) {
            throw new DataInputException("ID bàn mới phải là ký tự số");
        }

        Long oldTableId = Long.parseLong(oldTableIdStr);

        Long newTableId = Long.parseLong(newTableIdStr);

        Optional<AppTable> optionalAppTable = tableService.findById(newTableId);

        if (!optionalAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppTable newAppTable = optionalAppTable.get();

        ETableStatus eTableStatus = ETableStatus.fromString(newAppTable.getStatus().toString().toUpperCase());

        if (!eTableStatus.equals(ETableStatus.EMPTY)) {
            throw new DataInputException("Trạng thái bàn mới không hợp lệ, vui lòng kiểm tra lại.");
        }


        tableService.changeAllProductToNewTable(oldTableId, newTableId);


        List<TableDTO> tableDTOs= tableService.getAllTableDTOWhereDeletedIsFalse();

//        if (tableCombine != null) {
//            TableDTO tableCombineDTO = tableCombine.toTableDTO();
//
//            Map<String, TableDTO> result = new HashMap<>();
//            result.put("tableCombine", tableCombineDTO);
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }

        return new ResponseEntity<>(tableDTOs, HttpStatus.OK);
    }

    @PostMapping("/combine-tables")
    public ResponseEntity<?> combineTables(HttpServletRequest request) throws IOException {

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String currentTableIdStr;
        String targetTableIdStr;

        try {
            currentTableIdStr = json.get("currentTableId").asText();
            targetTableIdStr = json.get("targetTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(currentTableIdStr)) {
            throw new DataInputException("ID bàn hiện tại phải là ký tự số");
        }

        if (!validateUtils.isNumberValid(targetTableIdStr)) {
            throw new DataInputException("ID bàn mục tiêu phải là ký tự số");
        }

        Long currentTableId = Long.parseLong(currentTableIdStr);

        Long targetTableId = Long.parseLong(targetTableIdStr);

        Optional<AppTable> optionalCurrentAppTable = tableService.findById(currentTableId);

        if (!optionalCurrentAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<AppTable> optionalTargetAppTable = tableService.findById(targetTableId);

        if (!optionalTargetAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppTable currentTable = optionalCurrentAppTable.get();

        AppTable targetTable = optionalTargetAppTable.get();

        ETableStatus eCurrentTableStatus = ETableStatus.fromString(currentTable.getStatus().toString().toUpperCase());

        ETableStatus eTargetTableStatus = ETableStatus.fromString(targetTable.getStatus().toString().toUpperCase());

        if (!eCurrentTableStatus.equals(ETableStatus.BUSY)) {
            throw new DataInputException("Trạng thái bàn hiện tại không hợp lệ, vui lòng kiểm tra lại.");
        }

        if (!eTargetTableStatus.equals(ETableStatus.BUSY)) {
            throw new DataInputException("Trạng thái bàn muốn gộp không hợp lệ, vui lòng kiểm tra lại.");
        }

        tableService.combineTable(currentTable, targetTable);

        TableDTO currentTableDTO = currentTable.toTableDTO();

        TableDTO targetTableDTO = targetTable.toTableDTO();

        Map<String, TableDTO> result = new HashMap<>();
        result.put("currentTable", currentTableDTO);
        result.put("targetTable", targetTableDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/un-combine-tables")
    public ResponseEntity<?> unCombineTables(HttpServletRequest request) throws IOException {

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String currentTableIdStr;

        try {
            currentTableIdStr = json.get("currentTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(currentTableIdStr)) {
            throw new DataInputException("ID bàn hiện tại phải là ký tự số");
        }

        Long currentTableId = Long.parseLong(currentTableIdStr);

        Optional<AppTable> optionalCurrentAppTable = tableService.findById(currentTableId);

        if (!optionalCurrentAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppTable currentTable = optionalCurrentAppTable.get();

        ETableStatus eCurrentTableStatus = ETableStatus.fromString(currentTable.getStatus().toString().toUpperCase());

        if (!eCurrentTableStatus.equals(ETableStatus.COMBINE)) {
            throw new DataInputException("Trạng thái bàn hiện tại không hợp lệ, vui lòng kiểm tra lại.");
        }

        AppTable targetTable = tableService.unCombineTable(currentTable);

        TableDTO currentTableDTO = currentTable.toTableDTO();

        TableDTO targetTableDTO = targetTable.toTableDTO();

        Map<String, TableDTO> result = new HashMap<>();
        result.put("currentTable", currentTableDTO);
        result.put("targetTable", targetTableDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/close-table")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> closeTable(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String currentTableIdStr;

        try {
            currentTableIdStr = json.get("currentTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(currentTableIdStr)) {
            throw new DataInputException("ID bàn hiện tại phải là ký tự số");
        }

        Long currentTableId = Long.parseLong(currentTableIdStr);

        Optional<AppTable> tableOptional = tableService.findById(currentTableId);

        if (!tableOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppTable currentTable = tableService.closeTable(tableOptional.get());

        TableDTO currentTableDTO = currentTable.toTableDTO();

        Map<String, TableDTO> result = new HashMap<>();
        result.put("currentTable", currentTableDTO);


        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<AppTable> optionalAppTable = tableService.findById(id);

        if (!optionalAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tableService.delete(optionalAppTable.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

package com.cg.service.table;

import com.cg.domain.dto.table.TableCountDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.enums.ETableStatus;
import com.cg.service.IGeneralService;
import com.cg.domain.entity.table.AppTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITableService extends IGeneralService<AppTable, Long> {

    void saveTable(AppTable appTable);

    AppTable closeTable(AppTable appTable);

    List<TableDTO> getAllTableDTOWhereDeletedIsFalse();

    Page<TableDTO> findAllPageDeletedIsFalse(Pageable pageable);
    
    List<TableDTO> getTableDTOByStatusWhereDeletedIsFalse(ETableStatus status);

    void changeAllProductToNewTable(Long oldTableId, Long newTableId);

    void combineTable(AppTable currentTable, AppTable targetTable);

    AppTable unCombineTable(AppTable currentTable);

    TableCountDTO countTable ();


}

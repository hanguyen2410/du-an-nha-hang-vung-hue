package com.cg.repository;

import com.cg.domain.dto.table.TableCountDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.ETableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<AppTable, Long> {
    @Query("SELECT NEW com.cg.domain.dto.table.TableDTO (" +
                "tb.id, " +
                "tb.name, " +
                "tb.status " +
            ") " +
            "FROM AppTable AS tb " +
            "WHERE tb.deleted = false "
    )
    List<TableDTO> getAllTableDTOWhereDeletedIsFalse();

    @Query("SELECT NEW com.cg.domain.dto.table.TableDTO (" +
                "tb.id, " +
                "tb.name, " +
                "tb.status " +
            ") " +
            "FROM AppTable AS tb " +
            "WHERE tb.deleted = false "
    )
    Page<TableDTO> findAllPageDeletedIsFalse(Pageable pageable);

    @Query("SELECT NEW com.cg.domain.dto.table.TableDTO (" +
                "tb.id, " +
                "tb.name, " +
                "tb.status " +
            ") " +
            "FROM AppTable AS tb " +
            "WHERE tb.deleted = false " +
            "AND " +
            "tb.status = :status"
    )
    List<TableDTO> getTableDTOByStatusWhereDeletedIsFalse(@Param("status") ETableStatus status);

    @Query("SELECT NEW com.cg.domain.dto.table.TableCountDTO (" +
            "count(tb.id) " +
            ") " +
            "FROM AppTable AS tb " +
            "WHERE tb.deleted = false "
    )
    TableCountDTO countTable ();



}

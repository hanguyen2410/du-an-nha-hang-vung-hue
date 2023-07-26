package com.cg.repository;

import com.cg.domain.entity.backup.BillBackup;

import com.cg.domain.entity.table.AppTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BackupBillRepository extends JpaRepository<BillBackup, Long> {
    Optional<BillBackup> findByTableId(Long tableId);

    List<BillBackup> findAllByOrderId(Long orderId);
}


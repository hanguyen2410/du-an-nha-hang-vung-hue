package com.cg.service.backupBill;

import com.cg.domain.entity.backup.BillBackup;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IBackupBillService extends IGeneralService<BillBackup,Long> {

    Optional<BillBackup> findByTableId(Long tableId);
    List<BillBackup> findAllByOrderId(Long orderId);
}

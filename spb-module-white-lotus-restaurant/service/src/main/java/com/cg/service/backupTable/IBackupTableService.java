package com.cg.service.backupTable;

import com.cg.domain.entity.backup.TableBackup;
import com.cg.service.IGeneralService;



import java.util.Optional;

public interface IBackupTableService extends IGeneralService<TableBackup, Long> {

    Optional<TableBackup> findByTableCurrentIdAndOrderCurrentId(Long tableCurrentId, Long orderCurrentId);

    Optional<TableBackup> findByTableTargetId( Long tableTargetId);

    Optional<TableBackup> findByTableCurrentId(Long tableCurrentId);

    Optional<TableBackup> findByOrderCurrentId(Long orderCurrentId);
}

package com.cg.service.backupTable;


import com.cg.domain.entity.backup.TableBackup;
import com.cg.repository.TableBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BackupTableServiceImpl implements IBackupTableService {

    @Autowired
    private TableBackupRepository tableBackupRepository;

    @Override
    public List<TableBackup> findAll() {
        return tableBackupRepository.findAll();
    }

    @Override
    public TableBackup getById(Long aLong) {
        return null;
    }

    @Override
    public Optional<TableBackup> findById(Long id) {
        return tableBackupRepository.findById(id);
    }

    @Override
    public TableBackup save(TableBackup tableBackup) {
        return tableBackupRepository.save(tableBackup);
    }

    @Override
    public void delete(TableBackup tableBackup) {

    }

    @Override
    public void deleteById(Long id) {
        tableBackupRepository.deleteById(id);
    }

    @Override
    public Optional<TableBackup> findByTableCurrentIdAndOrderCurrentId(Long tableCurrentId, Long orderCurrentId) {
        return tableBackupRepository.findByTableCurrentIdAndOrderCurrentId(tableCurrentId, orderCurrentId);
    }

    @Override
    public Optional<TableBackup> findByTableTargetId(Long tableTargetId) {
        return tableBackupRepository.findByTableTargetId(tableTargetId);
    }

    @Override
    public Optional<TableBackup> findByTableCurrentId(Long tableCurrentId) {
        return tableBackupRepository.findByTableCurrentId(tableCurrentId);
    }

    @Override
    public Optional<TableBackup> findByOrderCurrentId(Long orderCurrentId) {
        return tableBackupRepository.findByOrderCurrentId(orderCurrentId);
    }
}

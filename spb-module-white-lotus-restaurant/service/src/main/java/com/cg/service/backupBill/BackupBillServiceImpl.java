package com.cg.service.backupBill;

import com.cg.domain.entity.backup.BillBackup;
import com.cg.repository.BackupBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BackupBillServiceImpl implements IBackupBillService{

    @Autowired
    BackupBillRepository backupBillRepository;

    @Override
    public List<BillBackup> findAll() {
        return backupBillRepository.findAll();
    }

    @Override
    public List<BillBackup> findAllByOrderId(Long orderId) {
        return backupBillRepository.findAllByOrderId(orderId);
    }

    @Override
    public BillBackup getById(Long aLong) {
        return null;
    }

    @Override
    public Optional<BillBackup> findById(Long id) {
        return findById(id);
    }

    @Override
    public BillBackup save(BillBackup billBackup) {
        return backupBillRepository.save(billBackup);
    }

    @Override
    public Optional<BillBackup> findByTableId(Long tableId) {
        return backupBillRepository.findByTableId(tableId);
    }

    @Override
    public void delete(BillBackup billBackup) {

    }

    @Override
    public void deleteById(Long id) {
        backupBillRepository.deleteById(id);
    }
}

package com.cg.repository;

import com.cg.domain.entity.backup.TableBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TableBackupRepository extends JpaRepository<TableBackup, Long> {

    @Query("SELECT NEW com.cg.domain.entity.backup.TableBackup (" +
                "tbu.id, " +
                "tbu.tableCurrentId, " +
                "tbu.orderCurrentId, " +
                "tbu.tableTargetId," +
                "tbu.orderTargetId " +
            ") " +
            "FROM TableBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId " +
            "AND tbu.orderCurrentId = :orderCurrentId "
    )
    Optional<TableBackup> findByTableCurrentIdAndOrderCurrentId(@Param("tableCurrentId") Long tableCurrentId, @Param("orderCurrentId") Long orderCurrentId);

    @Query("SELECT NEW com.cg.domain.entity.backup.TableBackup (" +
                "tbu.id, " +
                "tbu.tableCurrentId, " +
                "tbu.orderCurrentId, " +
                "tbu.tableTargetId," +
                "tbu.orderTargetId " +
            ") " +
            "FROM TableBackup AS tbu " +
            "WHERE tbu.tableTargetId = :tableTargetId "
    )
    Optional<TableBackup> findByTableTargetId(@Param("tableTargetId") Long tableTargetId);

    @Query("SELECT NEW com.cg.domain.entity.backup.TableBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId "
    )
    Optional<TableBackup> findByTableCurrentId(@Param("tableCurrentId") Long tableCurrentId);


    @Query("SELECT NEW com.cg.domain.entity.backup.TableBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableBackup AS tbu " +
            "WHERE tbu.orderCurrentId = :orderCurrentId "
    )
    Optional<TableBackup> findByOrderCurrentId(@Param("orderCurrentId") Long orderCurrentId);

}


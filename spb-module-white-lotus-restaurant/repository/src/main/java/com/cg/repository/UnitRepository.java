package com.cg.repository;


import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {

    @Query("SELECT NEW com.cg.domain.dto.unit.UnitDTO (" +
                "uni.id, " +
                "uni.title" +
            ")" +
            "FROM Unit AS uni "
    )
    List<UnitDTO> findAllUnitDTO();
}

package com.cg.service.unit;

import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.unit.Unit;
import com.cg.service.IGeneralService;

import java.util.List;

public interface IUnitService extends IGeneralService<Unit, Long> {

    List<UnitDTO> findAllUnitDTO();

}

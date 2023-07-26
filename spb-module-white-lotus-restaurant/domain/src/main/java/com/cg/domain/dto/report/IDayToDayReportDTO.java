package com.cg.domain.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public interface IDayToDayReportDTO {

//    @JsonFormat(pattern = "yyy-MM-dd")
    String getDay();
    BigDecimal getTotalAmount();
}

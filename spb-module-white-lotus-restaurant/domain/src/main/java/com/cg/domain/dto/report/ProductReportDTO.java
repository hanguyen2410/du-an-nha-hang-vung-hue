package com.cg.domain.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductReportDTO {
    private Long id;

    private String title;

    private String fileFolder;

    private String fileName;

    private Long quantity;

    private BigDecimal amount;
}

package com.cg.domain.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillFilterReqDTO {

        private Integer currentPageNumber;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date billFrom;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date billTo;
        private String status;

}

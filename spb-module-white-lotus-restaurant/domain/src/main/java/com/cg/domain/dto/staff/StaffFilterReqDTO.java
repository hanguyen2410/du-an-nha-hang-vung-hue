package com.cg.domain.dto.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffFilterReqDTO {
    private String keyWord;
    private Boolean deleted;
    private Integer roleIdFilter;
    private Integer dayOfBirthday;
    private Integer monthOfBirthday;
    private Date birthDayFrom;
    private Date birthDayTo;
    private Date birthDay;
    private Integer currentPageNumber;
}

package com.cg.domain.dto.bill;

import com.cg.domain.entity.bill.BillStatus;
import com.cg.domain.enums.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillStatusDTO {
    @NotNull(message = "Pls choose status")
    private Long id;

    private String code;
    private EProductStatus name;


    public BillStatus toBillStatus() {
        return new BillStatus()
                .setId(id)
                .setCode(code)
                .setName(name)
                ;
    }
}

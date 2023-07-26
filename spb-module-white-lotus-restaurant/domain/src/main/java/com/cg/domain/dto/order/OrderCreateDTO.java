package com.cg.domain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreateDTO {
    private Long id;

    @Pattern(regexp = "^\\d+$", message = "Id bàn phải là số.")
    private Long tableId;

}

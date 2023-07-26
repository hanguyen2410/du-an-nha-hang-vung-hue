package com.cg.domain.dto.table;

import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableCreateDTO {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên bàn.")
    @Size(min = 4, max = 20, message = "Tên bàn có độ dài nằm trong khoảng 4 - 20 ký tự.")
    private String name;

    private AppTable toTable(){
        return new AppTable()
                .setId(id)
                .setName(name)
                .setStatus(ETableStatus.EMPTY);
    }

}

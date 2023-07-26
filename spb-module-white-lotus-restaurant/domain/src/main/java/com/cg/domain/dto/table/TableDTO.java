package com.cg.domain.dto.table;

import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableDTO {
    private Long id;
    private String name;
    private String status;
    private String statusValue;

    public TableDTO(Long id, String name, ETableStatus tableStatus) {
        this.id = id;
        this.name = name;
        this.status = String.valueOf(tableStatus);
        this.statusValue = tableStatus.getValue();
    }

    public AppTable toTable(){
        return new AppTable()
                .setId(id)
                .setName(name)
                .setStatus(ETableStatus.valueOf(status))
                ;
    }
}

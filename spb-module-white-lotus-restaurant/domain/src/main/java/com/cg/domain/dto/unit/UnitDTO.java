package com.cg.domain.dto.unit;

import com.cg.domain.entity.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnitDTO {

    private Long id;
    private String title;


    public Unit toUnit(){
        return new Unit()
                .setId(id)
                .setTitle(title);
    }
}

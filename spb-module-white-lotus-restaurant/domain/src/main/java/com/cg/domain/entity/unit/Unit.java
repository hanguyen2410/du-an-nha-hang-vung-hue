package com.cg.domain.entity.unit;

import com.cg.domain.dto.unit.UnitDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public UnitDTO toUnitDTO(){
        return new UnitDTO()
                .setId(id)
                .setTitle(title);
    }

}
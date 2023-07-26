package com.cg.domain.entity.table;

import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_tables")
public class AppTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETableStatus status;


    public TableDTO toTableDTO(){
        return new TableDTO()
                .setId(id)
                .setName(name)
                .setStatus(status.toString())
                .setStatusValue(status.getValue())
                ;

    }

}

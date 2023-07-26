package com.cg.domain.entity.backup;

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
@Table(name = "table_backup")
public class TableBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableCurrentId;

    private Long orderCurrentId;

    private Long tableTargetId;

    private Long orderTargetId;

}

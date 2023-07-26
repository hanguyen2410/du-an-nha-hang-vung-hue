package com.cg.domain.entity.bill;

import com.cg.domain.dto.bill.BillStatusDTO;
import com.cg.domain.entity.product.Product;
import com.cg.domain.enums.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bill_status")
@Accessors(chain = true)
public class BillStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EProductStatus name;

    @OneToMany(targetEntity = Product.class)
    private List<Product> products;

    public BillStatusDTO toBillStatusDTO() {
        return new BillStatusDTO()
                .setId(id)
                .setCode(code)
                .setName(name)
                ;
    }
}

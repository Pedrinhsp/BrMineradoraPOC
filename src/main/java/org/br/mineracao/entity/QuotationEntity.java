package org.br.mineracao.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="quotation")
@Data
@NoArgsConstructor
public class QuotationEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @Column(name="currency_price")
    private BigDecimal currencyPrice;

    @Column(name="pct_change")
    private String pctChange;

    private String pair;

}

package org.br.mineracao.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineracao.entity.QuotationEntity;

@ApplicationScoped
public class QuotationRepository implements PanacheRepository<QuotationEntity> {




}

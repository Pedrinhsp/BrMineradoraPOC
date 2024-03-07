package org.br.mineracao.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.br.mineracao.client.CurrencyPriceClient;
import org.br.mineracao.dto.CurrencyPriceDto;
import org.br.mineracao.dto.QuotationDto;
import org.br.mineracao.entity.QuotationEntity;
import org.br.mineracao.message.KafkaEvents;
import org.br.mineracao.repository.QuotationRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;

    public void getCurrencyPrice(){
        CurrencyPriceDto currencyPriceInfo = currencyPriceClient.getPriceByPair("USD-BRL");

        if(updateCurrentInfoPrice(currencyPriceInfo)){
            kafkaEvents.sendNewKafkaEvent(QuotationDto
                    .builder()
                    .currencyPrice(new BigDecimal(currencyPriceInfo.getUsdbrl().getBid()))
                    .date(new Date())
                    .build());
        }
    }

    private boolean updateCurrentInfoPrice(CurrencyPriceDto currencyPriceInfo) {

        BigDecimal currentPrice = new BigDecimal(currencyPriceInfo.getUsdbrl().getBid());
        boolean updatePrice = false;

        List<QuotationEntity> quotationList = quotationRepository.findAll().list();

            if(quotationList.isEmpty()){

                saveQuotation(currencyPriceInfo);
                updatePrice = true;

            } else {
                QuotationEntity lastDollarPrice = quotationList
                        .get(quotationList.size() -1);

                if(currentPrice.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()){
                    updatePrice = true;
                    saveQuotation(currencyPriceInfo);
                }

            }
        return updatePrice;
    }

    private void saveQuotation(CurrencyPriceDto currencyInfo){
        QuotationEntity quotation = new QuotationEntity();

        quotation.setDate(new Date());
        quotation.setCurrencyPrice(new BigDecimal(currencyInfo.getUsdbrl().getBid()));
        quotation.setPctChange(currencyInfo.getUsdbrl().getPctChange());
        quotation.setPair("USD-BRL");

    }

}

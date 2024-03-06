package org.br.mineracao.message;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineracao.dto.QuotationDto;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaEvents {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("quotation-channel")
    Emitter<QuotationDto> quotationRequestEmitter;

    public void sendNewKafkaEvent(QuotationDto quotation){

        LOG.info("-- Enviando Cotação para tópico Kafka --");
        quotationRequestEmitter.send(quotation).toCompletableFuture().join();
    }

}

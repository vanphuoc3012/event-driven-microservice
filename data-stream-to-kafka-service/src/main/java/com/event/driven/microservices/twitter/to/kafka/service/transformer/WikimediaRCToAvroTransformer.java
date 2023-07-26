package com.event.driven.microservices.twitter.to.kafka.service.transformer;

import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import org.springframework.stereotype.Component;

@Component
public class WikimediaRCToAvroTransformer {
    private WikimediaRCArvoModel getWikimediaRCAvroFromWikimediaJSONResponse(String json) {
        return null;
    }
}

package com.event.driven.microservices.twitter.to.kafka.service.transformer;

import com.event.driven.microservices.kafka.avro.model.RCLengthArvoModel;
import com.event.driven.microservices.kafka.avro.model.RCMetadataArvoModel;
import com.event.driven.microservices.kafka.avro.model.RCRevisionArvoModel;
import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import com.event.driven.microservices.twitter.to.kafka.service.dto.WikimediaRecentChangeDto;
import com.event.driven.microservices.twitter.to.kafka.service.dto.WikimediaRecentChangeDto.RCLength;
import com.event.driven.microservices.twitter.to.kafka.service.dto.WikimediaRecentChangeDto.RCMeta;
import com.event.driven.microservices.twitter.to.kafka.service.dto.WikimediaRecentChangeDto.RCRevision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class WikimediaRCToAvroTransformer {
    public WikimediaRCArvoModel getWikimediaRCAvroFromWikimediaJSONResponse(WikimediaRecentChangeDto data) {
        log.debug("Transforming to WikimediaRCArvoModel: {}", data);
        return WikimediaRCArvoModel.newBuilder()
                                   .setRcschema(data.getRcschema())
                                   .setMeta(getRCMetadataArvoModel(data.getMeta()))
                                   .setId(data.getId())//'//
                                   .setType(data.getType())
                                   .setRcnamespace(data.getNamespace())
                                   .setTitle(data.getTitle())
                                   .setTitleUrl(data.getTitleUrl())
                                   .setComment(data.getComment())
                                   .setTimestamp(String.valueOf(data.getTimestamp()))
                                   .setUser(data.getUser())
                                   .setBot(data.isBot())
                                   .setNotifyUrl(data.getNotifyUrl())
                                   .setMinor(data.isMinor())
                                   .setPatrolled(data.isPatrolled())
                                   .setLength(getRCLengthArvoModel(data.getLength()))
                                   .setRevision(getRCRevisionArvoModel(data.getRevision()))
                                   .setServerUrl(data.getServerUrl())
                                   .setServerName(data.getServerName())
                                   .setServerScriptPath(data.getServerScriptPath())
                                   .setWiki(data.getWiki())
                                   .setParsedcomment(data.getParsedcomment())
                                   .build();
    }

    private RCRevisionArvoModel getRCRevisionArvoModel(RCRevision revision) {
        return Objects.isNull(revision) ? null : RCRevisionArvoModel.newBuilder()
                                                                    .setNew$(revision.getRvnew())
                                                                    .setOld(revision.getOld())
                                                                    .build();
    }

    private RCMetadataArvoModel getRCMetadataArvoModel(RCMeta data) {
        return RCMetadataArvoModel.newBuilder()
                                  .setUri(data.getUri())
                                  .setRequestId(data.getRequestId())
                                  .setId(data.getId())
                                  .setDt(String.valueOf(data.getDt()))
                                  .setDomain(data.getDomain())
                                  .setStream(data.getStream())
                                  .setTopic(data.getTopic())
                                  .setPartition(String.valueOf(data.getPartition()))
                                  .setOffset(String.valueOf(data.getOffset()))
                                  .build();
    }

    private RCLengthArvoModel getRCLengthArvoModel(RCLength data) {
        return Objects.isNull(data) ? null : RCLengthArvoModel.newBuilder()
                                                              .setOld(data.getOld())
                                                              .setNew$(data.getLennew())
                                                              .build();
    }
}

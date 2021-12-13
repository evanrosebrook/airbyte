package io.airbyte.integrations.destination.bigquery.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.Schema;
import com.google.common.collect.ImmutableMap;
import io.airbyte.commons.json.Jsons;
import io.airbyte.integrations.base.JavaBaseConstants;
import io.airbyte.integrations.destination.StandardNameTransformer;
import io.airbyte.protocol.models.AirbyteRecordMessage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GcsCsvBigQueryRecordFormatter implements BigQueryRecordFormatter {

    @Override
    public JsonNode formatRecord(final Schema schema, final AirbyteRecordMessage recordMessage) {
        final long emittedAtMicroseconds = TimeUnit.MICROSECONDS.convert(recordMessage.getEmittedAt(), TimeUnit.MILLISECONDS);
        final String formattedEmittedAt = QueryParameterValue.timestamp(emittedAtMicroseconds).getValue();
        final JsonNode formattedData = StandardNameTransformer.formatJsonPath(recordMessage.getData());
        return Jsons.jsonNode(ImmutableMap.of(
                JavaBaseConstants.COLUMN_NAME_AB_ID, UUID.randomUUID().toString(),
                JavaBaseConstants.COLUMN_NAME_EMITTED_AT, formattedEmittedAt,
                JavaBaseConstants.COLUMN_NAME_DATA, Jsons.serialize(formattedData))
        );
    }
}

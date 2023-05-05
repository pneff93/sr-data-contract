# CC Data Contract

[Data Contract Documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/data-contracts.html)

# Register Schema with Rules

We register a schema with a defined rule:

```shell
curl --request POST --url 'https://psrc-00000.region.provider.confluent.cloud/subjects/sensor-data-raw-value/versions'   \
  --header 'Authorization: Basic REPLACE_BASIC_AUTH' \
  --header 'content-type: application/octet-stream' \
  --data '{
            "schemaType": "AVRO",
            "schema": "{\"type\":\"record\",\"name\":\"SensorData\",\"namespace\":\"com.kafkaStreamsExample\",\"fields\":[{\"name\":\"sensorId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"type\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"value\",\"type\":\"float\"},{\"name\":\"unit\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"timestamp\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"]}]}",
            "metadata": {
            "properties": {
            "owner": "Patrick Neff",
            "email": "pneff@confluent.io"
            }
        },
        "ruleSet": {
        "domainRules": [
            {
            "name": "checkSensor",
            "kind": "CONDITION",
            "type": "CEL",
            "mode": "WRITE",
            "expr": "message.sensorId == \"sensor_1\"",
            "onFailure": "DLQ"
            }
            ]
        }
    }' 
```

## Check Schemas
```shell
curl --request GET \
  --url 'https://psrc-00000.region.provider.confluent.cloud/subjects/sensor-data-raw-value/versions/latest' \
  --header 'Authorization: Basic REPLACE_BASIC_AUTH' \ | jq
```

## Run

Ensure to create the topics sensor-data-raw and dlq-topic in CC in advance.

```
./gradlew run
```
The producer produces events continuously.
The first 5 records pass the rule and are sent to the sensor-data-raw topic. The second 5 records fail and are sent to the dlq-topic.

The SR in CC is able to work with Data Contracts but the UI is not able to display it.

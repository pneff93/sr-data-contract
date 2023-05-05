# CP Data Contract

[Data Contract Documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/data-contracts.html)

# Set up environment
```shell
docker-compose up -d
```

# Register Schema with Rules
We register a schema with a defined rule:

```shell
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
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
    }"' \
http://localhost:8081/subjects/sensor-data-raw-value/versions
```

## Check Schemas
```shell
curl -X GET http://localhost:8081/schemas | jq
```

## Run
```shell
 ./gradlew run  
```
The producer produces events continuously. The first 5 records pass the rule and are sent to the sensor-data-raw topic. The second 5 records fail and are sent to the dlq-topic.

Check C3 under `localhost:9091`.
However, C3 only displays the schema fields not the rules etc. (yet)


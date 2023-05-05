# CP Data Contract

[![LinkedIn][linkedin-shield]][linkedin-url]

[Data Contract Documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/data-contracts.html)


# Register Schema with Rules

```shell
curl --request POST --url 'https://psrc-4kk0p.westeurope.azure.confluent.cloud/subjects/sensor-data-raw-value/versions'   \
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
  --url 'https://psrc-4kk0p.westeurope.azure.confluent.cloud/schemas/ids/23' \
  --header 'Authorization: Basic REPLACE_BASIC_AUTH' \ | jq
```

## Findings
The SR in CC is able to work with Data Contracts but the UI is not able to display it.

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/patrick-neff-7bb3b21a4/

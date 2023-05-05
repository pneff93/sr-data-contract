import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import org.apache.kafka.clients.producer.ProducerConfig
import java.util.*

class ProducerProperties {

    fun configureProperties() : Properties{

        val settings = Properties()
//        settings.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        settings.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")
        settings.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        settings.setProperty("schema.registry.url", "http://schema-registry:8081")

        // Data Contracts, started in 7.4
        // Validation
        settings.setProperty("rule.executors", "checkSensor")
        settings.setProperty("rule.executors.checkSensor.class", "io.confluent.kafka.schemaregistry.rules.cel.CelExecutor")

        // Action
        settings.setProperty("rule.actions", "checkSensor")
        settings.setProperty("rule.actions.checkSensor.class", "io.confluent.kafka.schemaregistry.rules.DlqAction")
        settings.setProperty("rule.actions.checkSensor.param.topic", "dlq-topic")
        settings.setProperty("rule.actions.checkSensor.param.bootstrap.servers", "localhost:9092")

        // Required since we manually create schemas
        settings.setProperty("use.latest.version", "true")
        settings.setProperty("auto.register.schemas","false")

        // Required to produce the key to the DLQ
        settings.setProperty("key.serializer", "io.confluent.kafka.serializers.WrapperKeySerializer")
        settings.setProperty("wrapped.key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        return settings
    }
}
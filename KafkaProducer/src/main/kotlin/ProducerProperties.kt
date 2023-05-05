import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import java.util.*

class ProducerProperties {

    fun configureProperties() : Properties{

        val settings = Properties()
//        settings.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        settings.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")

        val api_key = "<Key>"
        val api_secret = "<Secret>"
        val broker = "<Broker Endpoint>"


        settings.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "$broker")
        settings.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
        settings.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")

        settings.setProperty(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='$api_key' password='$api_secret';")

        settings.setProperty("schema.registry.url", "<SR Endpoint>")
        settings.setProperty("basic.auth.credentials.source", "USER_INFO")
        settings.setProperty("schema.registry.basic.auth.user.info", "<Key>:<Secret>")

        // Data Contracts
        // Validation
        settings.setProperty("rule.executors", "checkSensor")
        settings.setProperty("rule.executors.checkSensor.class", "io.confluent.kafka.schemaregistry.rules.cel.CelExecutor")

        // Action
        settings.setProperty("rule.actions", "checkSensor")
        settings.setProperty("rule.actions.checkSensor.class", "io.confluent.kafka.schemaregistry.rules.DlqAction")
        settings.setProperty("rule.actions.checkSensor.param.topic", "dlq-topic")
        settings.setProperty("rule.actions.checkSensor.param.bootstrap.servers", "$broker")
        settings.setProperty("rule.actions.checkSensor.param.security.protocol","SASL_SSL")
        settings.setProperty("rule.actions.checkSensor.param.sasl.mechanism","PLAIN")
        settings.setProperty("rule.actions.checkSensor.param.sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='$api_key' password='$api_secret';")

        // Required since we manually create schemas
        settings.setProperty("use.latest.version", "true")
        settings.setProperty("auto.register.schemas","false")

        // Required to produce the key to the DLQ
        settings.setProperty("key.serializer", "io.confluent.kafka.serializers.WrapperKeySerializer")
        settings.setProperty("wrapped.key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        return settings
    }
}
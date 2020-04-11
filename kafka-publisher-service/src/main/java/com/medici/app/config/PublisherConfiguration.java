package com.medici.app.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.medici.app.model.BaseModel;

@Configuration
@EnableKafka
public class PublisherConfiguration {

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${kafka.tunnel.group}")
	private String tunnelGroup;

	@Value("${kafka.topic.consumer}")
	private String jsonTopicReply;

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, tunnelGroup);

		return props;
	}

	@Bean
	public ReplyingKafkaTemplate<String, BaseModel, BaseModel> replyKafkaTemplate() {
		ReplyingKafkaTemplate<String, BaseModel, BaseModel> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory(), replyContainer());
		replyingKafkaTemplate.setSharedReplyTopic(false);
		replyingKafkaTemplate.start();
		return replyingKafkaTemplate;
	}

	@Bean
	public KafkaMessageListenerContainer<String, BaseModel> replyContainer() {
		ContainerProperties containerProperties = new ContainerProperties(jsonTopicReply);
		return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, BaseModel>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, BaseModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setAutoStartup(true);
		factory.setBatchListener(false);
		return factory;
	}

	@Bean
	public ConsumerFactory<String, BaseModel> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(BaseModel.class));
	}

	@Bean
	public ProducerFactory<String, BaseModel> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

}

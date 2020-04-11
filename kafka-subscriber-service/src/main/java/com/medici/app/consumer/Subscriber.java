package com.medici.app.consumer;

import java.util.Calendar;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.medici.app.model.Company;

@Component
public class Subscriber implements BaseSubscriber<String, Company> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);

	@KafkaListener(topics = "${kafka.topic.consumer}")
	@SendTo("${kafka.topic.producer}")
	@Override
	public Company receive(ConsumerRecord<String, Company> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
		LOGGER.info(RECEIVED_KEY, record.key(), record.offset(), record.partition(), record.topic(), record.value().toString());
		record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);
		record.value().setConsumed(Calendar.getInstance().getTime());
		return record.value();
	}

}

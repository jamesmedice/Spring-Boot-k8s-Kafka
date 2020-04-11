package com.medici.app.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface BaseSubscriber<K, V> {

	public V receive(ConsumerRecord<K, V> recored, byte[] correlationId);

	public static final String RECEIVED_KEY = "received key {}, offset {}, partition {}, topic {} , object {}";
	public static final String OBJECT_NOT_FOUND = "object not found";
}

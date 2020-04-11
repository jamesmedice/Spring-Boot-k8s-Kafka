package com.medici.app.producer;

public interface BasePublisher<T> {

	public T send(T entity);

	public static final String SENT_OBJECT = "sent {}='{}'";
	public static final String EXPECTED_OBJECT = "expect object {}='{}'";
	public static final String RECEIVED_OBJECT = "retrieved back {}='{}'";

}

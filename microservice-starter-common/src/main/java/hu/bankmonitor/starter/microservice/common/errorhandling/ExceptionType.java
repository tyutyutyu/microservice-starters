package hu.bankmonitor.starter.microservice.common.errorhandling;

public enum ExceptionType {

	EVENT_CLASS_FINDER_CLASS_LOADING_ERROR,

	/**
	 * Message handlers handling event types that do not inherit from AbstractEvent
	 */
	EVENT_CLASS_FINDER_FIND_EVENT_CLASSES_ERROR,

	/**
	 * Message handlers' method(s) must have exactly one parameter
	 */
	EVENT_CLASS_FINDER_WRONG_METHOD_SIGNITURE,

	/**
	 * Amqp proxy service cannot be created
	 */
	AMQP_PROXY_CREATING_ERROR,

	/**
	 * Problem while invoking the handleMessage implementation
	 */
	RABBITMQ_MESSAGE_HANDLING_ERROR,

	UNKNOWN_ERROR;

}

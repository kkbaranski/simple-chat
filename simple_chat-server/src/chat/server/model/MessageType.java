package chat.server.model;

/**
 * @author Krzysztof Baranski
 */
public enum MessageType
{
	PLAIN,
	PLAINALL,
	UPDATE_NAME,
	UPDATE_CLIENT,
	REMOVE_CLIENT,
	LIST_CLIENTS,
	HELP,
	ERROR
}

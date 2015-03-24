package chat.client.model;

/**
 * @author Krzysztof Baranski
 */
public class Message
{
	private final MessageType type;
	private final int userId;
	private final String text;

	public Message( MessageType type, int userId, String text ) {
		this.type = type;
		this.userId = userId;
		this.text = text;
	}

	public MessageType getType() {
		return type;
	}

	public int getUserId() {
		return userId;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "Message{" +
		       "type=" + type +
		       ", userId=" + userId +
		       ", text='" + text + '\'' +
		       '}';
	}
}

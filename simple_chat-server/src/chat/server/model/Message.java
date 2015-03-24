package chat.server.model;

/**
 * @author Krzysztof Baranski
 */
public class Message
{
	private final MessageType type;
	private final int clientId;
	private final String text;

	public Message( MessageType type, int clientId, String text ) {
		this.type = type;
		this.clientId = clientId;
		this.text = text;
	}

	public MessageType getType() {
		return type;
	}

	public int getClientId() {
		return clientId;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;

		Message message = (Message) o;

		return clientId == message.clientId &&
		       ( text != null ? text.equals( message.text ) : message.text == null ) &&
		       type == message.type;
	}

	@Override
	public int hashCode() {
		int result = type.hashCode();
		result = 31 * result + clientId;
		result = 31 * result + ( text != null ? text.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "chat.server.model.Message{" +
		       "type=" + type +
		       ", clientId=" + clientId +
		       ", text='" + text + '\'' +
		       '}';
	}
}

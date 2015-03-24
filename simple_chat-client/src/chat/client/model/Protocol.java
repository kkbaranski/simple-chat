package chat.client.model;

/**
 * @author Krzysztof Baranski
 */
public interface Protocol
{
	public Message parseLine( String line );

	public String createLine( Message message );
}

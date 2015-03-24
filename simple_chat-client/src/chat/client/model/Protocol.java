package chat.client.model;

import chat.client.model.Message;

/**
 * @author Krzysztof Baranski
 */
public interface Protocol
{
	public Message parseLine( String line );

	public String createLine( Message message );
}

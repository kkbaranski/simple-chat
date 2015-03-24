package chat.server.protocol;

import chat.server.model.Message;

import java.util.regex.Pattern;

/**
 * @author Krzysztof Baranski
 */
public interface Protocol
{
	public Pattern getNamePattern();

	public String toString( Message message );

	public Message parseLine( String line );
}

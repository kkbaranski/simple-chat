package chat.client.model;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krzysztof Baranski
 */
public class MyProtocol implements Protocol
{
	private static final String idGroup = "id";
	private static final String nameGroup = "username";
	private static final String messageGroup = "message";

	private static final Pattern idPattern = Pattern.compile( "(?<" + idGroup + ">\\d+)" );
	private static final Pattern namePattern = Pattern.compile( "(?<" + nameGroup + ">\\p{Alnum}+)" );
	private static final Pattern messagePattern = Pattern.compile( "(?<" + messageGroup + ">.*)" );

	private static final Pattern plainMessagePattern = Pattern.compile( "^<" +
	                                                                    idPattern.pattern() +
	                                                                    ":\\s*" +
	                                                                    messagePattern.pattern() +
	                                                                    "\\s*$" );
	private static final Pattern updateUserPattern = Pattern.compile( "^@" +
	                                                                  idPattern.pattern() +
	                                                                  "=" +
	                                                                  namePattern.pattern() +
	                                                                  "$" );
	private static final Pattern removeUserPattern = Pattern.compile( "^~" + idPattern.pattern() + "$" );
	private static final Pattern errorPattern = null;


	public Message parseLine( String line ) {
		logger.info( "parsing line: " + line );

		Message message;
		Matcher matcher;
		if( plainMessagePattern != null && ( matcher = plainMessagePattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.PLAIN,
			                       Integer.parseInt( matcher.group( idGroup ) ),
			                       matcher.group( messageGroup ) );
		} else if( updateUserPattern != null && ( matcher = updateUserPattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.UPDATE_USER,
			                       Integer.parseInt( matcher.group( idGroup ) ),
			                       matcher.group( nameGroup ) );
		} else if( removeUserPattern != null && ( matcher = removeUserPattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.REMOVE_USER, Integer.parseInt( matcher.group( idGroup ) ), null );
		} else if( errorPattern != null && ( matcher = errorPattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.ERROR, 0, "Line '" + line + "' cannot be parsed!" );
		} else {
			message = new Message( MessageType.ERROR, 0, "Line cannot be parsed" );
		}

		logger.info( message.toString() );
		return message;
	}

	public String createLine( Message message ) {
		logger.info( message.toString() );
		String line = "";
		switch( message.getType() ) {
			case PLAIN:
				line = ">" + message.getUserId() + ":" + message.getText();
				break;
			case PLAINALL:
				line = ">:" + message.getText();
				break;
			case TEXT:
				line = message.getText();
				break;
			case UPDATE_USER:
				line = "@" + message.getText();
				break;
		}
		logger.info( "line: " + line );
		return line;
	}

	private static final Logger logger = Logger.getLogger( Protocol.class.getName() );
}

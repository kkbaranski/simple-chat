package chat.server.protocol;

import chat.server.model.Message;
import chat.server.model.MessageType;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krzysztof Baranski
 */
public class MyProtocol implements Protocol
{
	private static final String idGroup = "id";
	private static final String nameGroup = "name";
	private static final String messageGroup = "message";

	private static final Pattern idPattern = Pattern.compile( "(?<" + idGroup + ">\\d+)" );
	private static final Pattern namePattern = Pattern.compile( "(?<" + nameGroup + ">\\p{Alnum}+)" );
	private static final Pattern messagePattern = Pattern.compile( "(?<" + messageGroup + ">.*)" );

	private static final Pattern plainMessagePattern = Pattern.compile( "^>" +
	                                                                    idPattern.pattern() +
	                                                                    ":\\s*" +
	                                                                    messagePattern.pattern() +
	                                                                    "\\s*$" );
	private static final Pattern plainAllMessagePattern = Pattern.compile( "^>:\\s*" +
	                                                                       messagePattern.pattern() +
	                                                                       "\\s*$" );
	private static final Pattern updateNamePattern = Pattern.compile( "^@" + namePattern.pattern() + "$" );
	private static final Pattern listClientsPattern = Pattern.compile( "^list$" );
	private static final Pattern helpPattern = Pattern.compile( "^\\?$" );
	private static final Pattern errorPattern = null;

	@Override
	public Pattern getNamePattern() {
		return namePattern;
	}

	@Override
	public String toString( Message message ) {
		logger.info( "Parsing message: " + message );
		String line = null;
		switch( message.getType() ) {
			case PLAIN:
				line = "<" + message.getClientId() + ":" + message.getText();
				break;
			case UPDATE_CLIENT:
				line = "@" + message.getClientId() + "=" + message.getText();
				break;
			case REMOVE_CLIENT:
				line = "~" + message.getClientId();
				break;
			case HELP:
				line = helpString( message.getClientId(), message.getText() );
				break;
			case ERROR:
				line = "ERROR: " + message.getText();
		}
		logger.info( "line: " + line );
		return line;
	}

	public Message parseLine( String line ) {
		logger.info( "Parsing line: " + line );

		Message message;
		Matcher matcher;
		if( plainMessagePattern != null && ( matcher = plainMessagePattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.PLAIN,
			                       Integer.parseInt( matcher.group( idGroup ) ),
			                       matcher.group( messageGroup ) );
		} else if( plainAllMessagePattern != null && ( matcher = plainAllMessagePattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.PLAINALL, 0, matcher.group( messageGroup ) );
		} else if( updateNamePattern != null && ( matcher = updateNamePattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.UPDATE_NAME, 0, matcher.group( nameGroup ) );
		} else if( listClientsPattern != null && ( matcher = listClientsPattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.LIST_CLIENTS, 0, null );
		} else if( helpPattern != null && ( matcher = helpPattern.matcher( line ) ).matches() ) {
			message = new Message( MessageType.HELP, 0, null );
		} else {
			message = new Message( MessageType.ERROR, 0, parsingErrorString() );
		}
		logger.info( message.toString() );
		return message;
	}

	private String parsingErrorString() {
		return "Line cannot be parsed";
	}

	private String helpString( int clientId, String clientName ) {
		return clientName + " (" + clientId + ")\n" +
		       "INFO:\n" +
		       "\t@ID=USERNAME\t-user USERNAME has been assigned to ID\n" +
		       "\t<ID:MESSAGE\t-ID user's sent MESSAGE to you\n" +
		       "\t~ID\t\t-ID user's gone out" +
		       "\n" +
		       "COMMANDS:\n" +
		       "\t@USERNAME\t-change your username to USERNAME\n" +
		       "\t?\t\t-this help\n" +
		       "\t>ID:MESSAGE\t-send MESSAGE to ID user\n" +
		       "\t>:MESSAGE\t-send MESSAGE to all users\n" +
		       "\tlist\t\t-send user definitions again";
	}


	private static final Logger logger = Logger.getLogger( Protocol.class.getName() );
}

import chat.server.model.Message;
import chat.server.model.MessageType;
import chat.server.protocol.MyProtocol;
import chat.server.protocol.Protocol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MyProtocolTest
{
	Protocol protocol;

	@Before
	public void setUp() throws Exception {
		protocol = new MyProtocol();
	}

	@Test
	public void testGetNamePattern() throws Exception {
		assertEquals( "(?<name>\\p{Alnum}+)", protocol.getNamePattern().pattern() );
	}

	@Test
	public void testToString() throws Exception {
		Message m1 = new Message( MessageType.PLAIN, 123, "sometext" );
		Message m2 = new Message( MessageType.PLAINALL, 123, "sometext" );
		Message m3 = new Message( MessageType.UPDATE_NAME, 123, "sometext" );
		Message m4 = new Message( MessageType.UPDATE_CLIENT, 123, "sometext" );
		Message m5 = new Message( MessageType.REMOVE_CLIENT, 123, "sometext" );
		Message m6 = new Message( MessageType.LIST_CLIENTS, 123, "sometext" );
		Message m7 = new Message( MessageType.HELP, 123, "sometext" );
		Message m8 = new Message( MessageType.ERROR, 123, "sometext" );

		assertEquals( "<123:sometext", protocol.toString( m1 ) );
		assertNull( protocol.toString( m2 ) );
		assertNull( protocol.toString( m3 ) );
		assertEquals( "@123=sometext", protocol.toString( m4 ) );
		assertEquals( "~123", protocol.toString( m5 ) );
		assertNull( protocol.toString( m6 ) );
		assertEquals( "SYNTAX:\n" +
		              "\t@ID=USERNAME\t-user USERNAME has been assigned to ID\n" +
		              "\t<ID:MESSAGE\t-ID user's sent MESSAGE to you\n" +
		              "\t~ID\t\t-ID user's gone out" +
		              "\n" +
		              "COMMANDS:\n" +
		              "\t@USERNAME\t-change your username to USERNAME\n" +
		              "\t?\t\t-this help\n" +
		              "\t>ID:MESSAGE\t-send MESSAGE to ID user\n" +
		              "\t>:MESSAGE\t-send MESSAGE to all users\n" +
		              "\tlist\t\t-send user definitions again", protocol.toString( m7 ) );
		assertNull( protocol.toString( m8 ) );
	}

	@Test
	public void testParseLine() throws Exception {
		String line1 = ">123:sometext";
		String line2 = ">:sometext";
		String line3 = "@sometext";
		String line4 = "~123";
		String line5 = "?";
		String line6 = "help";
		String line7 = "list";
		String line8 = "sbdvlksdbvrusijbvskd";
		String line9 = "";

		assertEquals( new Message( MessageType.PLAIN, 123, "sometext"), protocol.parseLine( line1 ) );
		assertEquals( new Message(MessageType.PLAINALL, 0, "sometext"), protocol.parseLine( line2 ) );
		assertEquals( new Message(MessageType.UPDATE_NAME, 0, "sometext"), protocol.parseLine( line3 ) );
		assertEquals( new Message(MessageType.ERROR, 0, "Line cannot be parsed"), protocol.parseLine( line4 ) );
		assertEquals( new Message(MessageType.HELP, 0, null), protocol.parseLine( line5 ) );
		assertEquals( new Message(MessageType.ERROR, 0, "Line cannot be parsed"), protocol.parseLine( line6 ) );
		assertEquals( new Message(MessageType.LIST_CLIENTS, 0, null), protocol.parseLine( line7 ) );
		assertEquals( new Message(MessageType.ERROR, 0, "Line cannot be parsed"), protocol.parseLine( line8 ) );
		assertEquals( new Message(MessageType.ERROR, 0, "Line cannot be parsed"), protocol.parseLine( line9 ) );
	}
}
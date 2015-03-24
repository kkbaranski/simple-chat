package chat.client;

import chat.client.model.MyProtocol;
import chat.client.model.Protocol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyProtocolTest
{
	private Protocol myProtocol;

	@Before
	public void init() {
		myProtocol = new MyProtocol();
	}

	@Test
	public void testParseLine() throws Exception {
		String test01 = "@123=Ala";
		String test02 = "@123=1";
		String test03 = "<123:sdfs";
		String test04 = "<123:";
		String test05 = "~23";
		String test06 = "sdfgdsfg";


		assertEquals( "Message{type=UPDATE_USER, userId=123, text='Ala'}", myProtocol.parseLine( test01 ).toString() );
		assertEquals( "Message{type=UPDATE_USER, userId=123, text='1'}", myProtocol.parseLine( test02 ).toString() );
		assertEquals( "Message{type=PLAIN, userId=123, text='sdfs'}", myProtocol.parseLine( test03 ).toString() );
		assertEquals( "Message{type=PLAIN, userId=123, text=''}", myProtocol.parseLine( test04 ).toString() );
		assertEquals( "Message{type=REMOVE_USER, userId=23, text='null'}", myProtocol.parseLine( test05 ).toString() );
		assertEquals( "Message{type=ERROR, userId=0, text='Line cannot be parsed'}", myProtocol.parseLine( test06 ).toString() );
	}

	@Test
	public void testCreateLine() throws Exception {

	}
}
package room13.message;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

public class MessageStreamTest {

	@Test
	public void testMessageEncodeAndDecode() throws IOException, RemoteConnectionClosedException {
		RawMessage msg = new RawMessage();
		msg.setMsgId(1);
		msg.setReqId(10);
		msg.addValue("val1");
		msg.addValue("val2");
		msg.setDictValue("key1", "val1");
		msg.setDictValue("key2", "val2");
		msg.setBody(BodyCoder.encodeText("This is a test"));
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		MessageWriter writer = new MessageWriter(os);
		writer.write(msg);
		
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		
		MessageReader reader = new MessageReader(is);
		RawMessage received = reader.read();
		
		assertEquals("msg id correct", 1, received.getMsgId());
		assertEquals("req id correct",10, received.getReqId());
		assertEquals("resp id correct",0, received.getRespId());
		assertEquals("value params size correct",2,received.getValueParams().size());
		assertTrue("value param 1 correct","val1".equals(received.getValue(0)));
		assertTrue("value param 2 correct","val2".equals(received.getValue(1)));
		assertTrue("key param 1 correct","val1".equals(received.getDictValue("key1")));
		assertTrue("key param 2 correct","val2".equals(received.getDictValue("key2")));
		assertTrue("body correct","This is a test".equals(BodyCoder.decodeText(received.getBody())));
		
	}

}

package room13.message;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BodyCoderTest {

	
	@Test
	public void testSimpleListCodec() throws IOException {
		List<String> myList = Arrays.asList("one","two","three");
		byte[] encodedList = BodyCoder.encodeSimpleList(myList);
		List<String> decodedList = BodyCoder.decodeSimpleList(encodedList);
		assertArrayEquals(myList.toArray(), decodedList.toArray());
	}

	@Test
	public void testTextCodec() {
		String text = "This is a test";
		byte[] encoded = BodyCoder.encodeText(text);
		String decoded = BodyCoder.decodeText(encoded);
		assertTrue(text.equals(decoded));
	}

}

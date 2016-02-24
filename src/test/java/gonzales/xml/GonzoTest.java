package gonzales.xml;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GonzoTest {
	private static final Logger log = LoggerFactory.getLogger(GonzoTest.class);

	/**
	 * File taken from
	 * https://msdn.microsoft.com/en-us/library/ms762271%28v=vs.85%29.aspx
	 */
	@Test
	public void books() throws GonzoException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("./books.xml");
		Gonza g = new Gonzo(is);
		String t = null;
		String p = null;
		while (g.stroll()) {
			// Meeting
			if (g.meets("/:catalog/:book/:title")) {
				t = g.pick();
			} else if (g.meets("/:catalog/:book/:price")) {
				p = g.pick();
			}

			// Leaving
			if (g.leaves("/:catalog/:book")) {
				// Print all
				log.info("{}: {}", t, p);
				// Resetting ...
				t = null;
				p = null;
			}

		}
	}
}

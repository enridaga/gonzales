package gonzales.xml;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

public interface Gonza {

	public abstract boolean meets(String path);

	public abstract boolean initialized();

	public abstract boolean leaves(String path);

	public abstract void init(InputStream is) throws GonzoException;

	public abstract boolean stroll() throws GonzoException;

	public abstract boolean hasAttribute(String name, String namespace);

	public abstract String getAttribute(String name);

	public abstract boolean hasAttribute(String name);

	public abstract String getAttribute(String name, String namespace);

	public abstract String pick();

	public abstract String pickXML();

}
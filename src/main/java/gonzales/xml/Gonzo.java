package gonzales.xml;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author enridaga
 *
 */
public class Gonzo implements Gonza {
	private static final Logger log = LoggerFactory.getLogger(Gonzo.class);
	protected XMLEvent event = null;
	private XMLEventReader eventReader;
	private String path = "";
	private boolean initialized = false;

	public Gonzo() {
	}

	public Gonzo(InputStream is) throws GonzoException {
		init(is);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#begins(java.lang.String)
	 */
	@Override
	public boolean meets(String path) {
		if (event.isStartElement() && path.equals(this.path)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#initialized()
	 */
	@Override
	public boolean initialized() {
		return initialized;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#ends(java.lang.String)
	 */
	@Override
	public boolean leaves(String path) {
		if (event.isEndElement() && path.equals(this.path)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#init(java.io.InputStream)
	 */
	@Override
	public void init(InputStream is) throws GonzoException {
		try {
			this.initialized = true;
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			this.eventReader = inputFactory.createXMLEventReader(is);
		} catch (XMLStreamException e) {
			throw new GonzoException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#traverse()
	 */
	@Override
	public boolean stroll() throws GonzoException {
		if (eventReader.hasNext()) {
			if (event != null && event.isEndElement()) {
				log.trace("{} ends", path);
				path = path.substring(0, path.lastIndexOf('/'));
			}
			try {
				event = eventReader.nextEvent();
			} catch (XMLStreamException e) {
				throw new GonzoException("Journey interrupted.", e);
			}
			if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				String name = se.getName().getPrefix() + ":"
						+ se.getName().getLocalPart();
				path += "/" + name;
				log.trace("{} starts", path);
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#hasAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasAttribute(String name, String namespace) {
		if (event.isStartElement()) {
			StartElement se = event.asStartElement();
			QName qn;
			if (namespace == null) {
				qn = new QName(name);
			} else {
				qn = new QName(name, namespace);
			}
			Attribute attr = se.getAttributeByName(qn);
			if (attr != null) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String name) {
		return getAttribute(name, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#hasAttribute(java.lang.String)
	 */
	@Override
	public boolean hasAttribute(String name) {
		return hasAttribute(name, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#getAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAttribute(String name, String namespace) {
		if (event.isStartElement()) {
			StartElement se = event.asStartElement();
			QName qn;
			if (namespace != null) {
				qn = new QName(name, namespace);
			} else {
				qn = new QName(name);
			}
			Attribute attr = se.getAttributeByName(qn);
			if (attr != null) {
				return attr.getValue();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#pick()
	 */
	@Override
	public String pick() {
		try {
			return getStringValue(eventReader);
		} catch (XMLStreamException e) {
			log.error("", e);
			return "";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gonzales.xml.Gonza#getXMLValue()
	 */
	@Override
	public String pickXML() {
		try {
			return getXHTMLValue(eventReader);
		} catch (XMLStreamException e) {
			log.error("", e);
			return "";
		}
	}

	private String getXHTMLValue(XMLEventReader eventReader)
			throws XMLStreamException {
		StringBuilder b = new StringBuilder();
		// event = eventReader.nextEvent();
		StartElement se;
		if (event == null || !event.isStartElement()) {
			throw new XMLStreamException("Expected start element");
		}
		se = event.asStartElement();
		event = eventReader.nextEvent();

		while (!(event.isEndElement() && event.asEndElement().getName()
				.equals(se.getName()))) {
			if (event.isStartElement()) {
				b.append('<');
				b.append(event.asStartElement().getName().getLocalPart());
				if (!(event.asStartElement().getName().getNamespaceURI()
						.equals("") || event.asStartElement().getName()
						.getNamespaceURI()
						.equals("http://www.w3.org/1999/xhtml"))) {
					b.append(" xmlns=\"");
					b.append(event.asStartElement().getName().getNamespaceURI());
					b.append("\"");
				}
				b.append('>');
			} else if (event.isEndElement()) {
				b.append('<').append('/');
				b.append(event.asEndElement().getName().getLocalPart());
				b.append('>');
			} else if (event.isCharacters()) {
				b.append(event.asCharacters().getData().trim());
			}
			event = eventReader.nextEvent();
		}
		return b.toString().trim();
	}

	private String getStringValue(XMLEventReader eventReader)
			throws XMLStreamException {
		if (event == null || !event.isStartElement()) {
			throw new XMLStreamException("Expected start element");
		}
		StringBuilder b = new StringBuilder();
		event = eventReader.nextEvent();
		while (event.isCharacters()) {
			b.append(event.asCharacters().getData().trim());
			event = eventReader.nextEvent();
		}
		String value = b.toString();
		return value;
	}

}

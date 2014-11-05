package no.difi.meldingsutveksling.dokumentpakking.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import no.difi.meldingsutveksling.dokumentpakking.domain.EncryptedContent;

import org.apache.commons.codec.binary.Base64;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payload")
public class Payload {
	public Payload(EncryptedContent payload) {
		this.asice = Base64.encodeBase64(payload.getContent());
		this.encryptionKey = new String(Base64.encodeBase64(payload.getKey()));
	}
	public Payload() {
		// Need no-arg constructor for JAXB
	}
	
	@XmlValue
	private byte[] asice;

	@XmlAttribute
	private String encryptionKey;

	public byte[] getAsice() {
		return asice;
	}
	public void setAsice(byte[] asice) {
		this.asice = asice;
	}
	public String getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	

}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.25 at 12:23:12 PM CET 
//


package no.difi.meldingsutveksling.domain.sbdh;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import no.difi.meldingsutveksling.nextmove.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactInformation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContactInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Contact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TelephoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ContactTypeIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInformation", propOrder = {
        "contact",
        "emailAddress",
        "faxNumber",
        "telephoneNumber",
        "contactTypeIdentifier"
})
@Getter
@Setter
@ToString
@Entity
@Table(name = "contact_information")
public class ContactInformation extends AbstractEntity<Long> {

    @XmlElement(name = "Contact", required = true)
    @NotNull
    protected String contact;

    @XmlElement(name = "EmailAddress")
    protected String emailAddress;

    @XmlElement(name = "FaxNumber")
    protected String faxNumber;

    @XmlElement(name = "TelephoneNumber")
    protected String telephoneNumber;

    @XmlElement(name = "ContactTypeIdentifier")
    protected String contactTypeIdentifier;
}

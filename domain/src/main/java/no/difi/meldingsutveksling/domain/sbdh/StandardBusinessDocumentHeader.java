//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.25 at 12:23:12 PM CET 
//


package no.difi.meldingsutveksling.domain.sbdh;

import no.difi.meldingsutveksling.domain.MeldingsUtvekslingRuntimeException;
import no.difi.meldingsutveksling.domain.Organisasjonsnummer;
import no.difi.meldingsutveksling.domain.XMLTimeStamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.*;


/**
 * <p>Java class for StandardBusinessDocumentHeader complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="StandardBusinessDocumentHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HeaderVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sender" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Partner" maxOccurs="unbounded"/>
 *         &lt;element name="Receiver" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Partner" maxOccurs="unbounded"/>
 *         &lt;element name="DocumentIdentification" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}DocumentIdentification"/>
 *         &lt;element name="Manifest" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Manifest" minOccurs="0"/>
 *         &lt;element name="BusinessScope" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}BusinessScope" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StandardBusinessDocumentHeader", propOrder = {
        "headerVersion",
        "sender",
        "receiver",
        "documentIdentification",
        "manifest",
        "businessScope"
})
public class StandardBusinessDocumentHeader {

    public enum DocumentType {KVITTERING, MELDING}

    public static final String STANDARD_IDENTIFIER = "urn:no:difi:meldingsutveksling:1.0";
    public static final String KVITTERING_TYPE = "kvittering";
    public static final String KVITTERING_VERSION = "urn:no:difi:meldingsutveksling:1.0";
    public static final String MELDING_TYPE = "melding";
    public static final String MELDING_VERSION = "urn:no:difi:meldingsutveksling:1.0";

    @XmlElement(name = "HeaderVersion", required = true)
    protected String headerVersion;
    @XmlElement(name = "Sender", required = true)
    protected List<Partner> sender;
    @XmlElement(name = "Receiver", required = true)
    protected List<Partner> receiver;
    @XmlElement(name = "DocumentIdentification", required = true)
    protected DocumentIdentification documentIdentification;
    @XmlElement(name = "Manifest")
    protected Manifest manifest;
    @XmlElement(name = "BusinessScope")
    protected BusinessScope businessScope;

    /**
     * Gets the value of the headerVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHeaderVersion() {
        return headerVersion;
    }

    /**
     * Sets the value of the headerVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHeaderVersion(String value) {
        this.headerVersion = value;
    }

    /**
     * Gets the value of the sender property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sender property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSender().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Partner }
     */
    public List<Partner> getSender() {
        if (sender == null) {
            sender = new ArrayList<Partner>();
        }
        return this.sender;
    }

    /**
     * Gets the value of the receiver property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiver property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiver().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Partner }
     */
    public List<Partner> getReceiver() {
        if (receiver == null) {
            receiver = new ArrayList<Partner>();
        }
        return this.receiver;
    }

    public String getReceiverOrganisationNumber() {

        if (receiver.size() != 1) {
            throw new MeldingsUtvekslingRuntimeException(String.valueOf(receiver.size()));
        }
        Partner partner = receiver.get(0);
        PartnerIdentification identifier = partner.getIdentifier();
        if (identifier == null) {
            throw new MeldingsUtvekslingRuntimeException();
        }
        return identifier.getValue();
    }


    /**
     * Gets the value of the documentIdentification property.
     *
     * @return possible object is
     * {@link DocumentIdentification }
     */
    public DocumentIdentification getDocumentIdentification() {
        return documentIdentification;
    }

    /**
     * Sets the value of the documentIdentification property.
     *
     * @param value allowed object is
     *              {@link DocumentIdentification }
     */
    public void setDocumentIdentification(DocumentIdentification value) {
        this.documentIdentification = value;
    }

    /**
     * Gets the value of the manifest property.
     *
     * @return possible object is
     * {@link Manifest }
     */
    public Manifest getManifest() {
        return manifest;
    }

    /**
     * Sets the value of the manifest property.
     *
     * @param value allowed object is
     *              {@link Manifest }
     */
    public void setManifest(Manifest value) {
        this.manifest = value;
    }

    /**
     * Gets the value of the businessScope property.
     *
     * @return possible object is
     * {@link BusinessScope }
     */
    public BusinessScope getBusinessScope() {
        return businessScope;
    }

    /**
     * Sets the value of the businessScope property.
     *
     * @param value allowed object is
     *              {@link BusinessScope }
     */
    public void setBusinessScope(BusinessScope value) {
        this.businessScope = value;
    }

    public void setSender(List<Partner> sender) {
        this.sender = sender;
    }

    public void setReceiver(List<Partner> receiver) {
        this.receiver = receiver;
    }


    public static class Builder {

        private static final String HEADER_VERSION = "1.0";

        private static final String TYPE_JOURNALPOST_ID = "JournalpostId";
        private static final String TYPE_CONVERSATIONID = "ConversationId";

        private Organisasjonsnummer avsender;
        private Organisasjonsnummer mottaker;
        private String journalPostId;
        private String conversationId;
        private DocumentType documentType;

        public Builder from(Organisasjonsnummer avsender) {
            this.avsender = avsender;
            return this;
        }

        public Builder to(Organisasjonsnummer mottaker) {
            this.mottaker = mottaker;
            return this;
        }

        public Builder relatedToJournalPostId(String journalPostId) {
            this.journalPostId = journalPostId;
            return this;
        }

        public Builder type(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder relatedToConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public StandardBusinessDocumentHeader build() {
            if (documentType == null) {
                throw new MeldingsUtvekslingRuntimeException("DocumentType must be set");
            }
            StandardBusinessDocumentHeader header = new StandardBusinessDocumentHeader();
            header.setHeaderVersion(HEADER_VERSION);
            header.getSender().add(createPartner(avsender));
            header.getReceiver().add(createPartner(mottaker));
            header.setBusinessScope(createBusinessScope(fromConversationId(conversationId), fromJournalPostId(journalPostId)));
            if (documentType == DocumentType.KVITTERING) {
                header.setDocumentIdentification(createDocumentIdentification(KVITTERING_TYPE, KVITTERING_VERSION));
            } else if (documentType == DocumentType.MELDING) {
                header.setDocumentIdentification(createDocumentIdentification(MELDING_TYPE, MELDING_VERSION));
            }
            return header;
        }

        private Partner createPartner(Organisasjonsnummer orgNummer) {
            Partner partner = new Partner();
            PartnerIdentification partnerIdentification = new PartnerIdentification();
            partnerIdentification.setValue(orgNummer.asIso6523());
            partnerIdentification.setAuthority(orgNummer.asIso6523());
            partner.setIdentifier(partnerIdentification);
            return partner;
        }

        private DocumentIdentification createDocumentIdentification(String type, String version) {
            DocumentIdentification documentIdentification = new DocumentIdentification();

            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(new Date());
            documentIdentification.setCreationDateAndTime(XMLTimeStamp.createTimeStamp());
            documentIdentification.setStandard(STANDARD_IDENTIFIER);
            documentIdentification.setType(type);
            documentIdentification.setTypeVersion(version);
            documentIdentification.setInstanceIdentifier(UUID.randomUUID().toString());
            return documentIdentification;
        }

        private BusinessScope createBusinessScope(Scope... scopes) {
            BusinessScope bScope = new BusinessScope();
            bScope.setScope(Arrays.asList(scopes));
            return bScope;
        }

        private Scope fromJournalPostId(String journalPostId) {
            Scope scope = createDefaultScope();
            scope.setType(TYPE_JOURNALPOST_ID);
            scope.setInstanceIdentifier(journalPostId);
            return scope;
        }

        private Scope fromConversationId(String conversationId) {
            Scope scope = createDefaultScope();
            scope.setType(TYPE_CONVERSATIONID);
            scope.setInstanceIdentifier(conversationId);
            return scope;
        }

        private Scope createDefaultScope() {
            Scope scope = new Scope();
            scope.setIdentifier(STANDARD_IDENTIFIER);
            return scope;
        }

    }
}
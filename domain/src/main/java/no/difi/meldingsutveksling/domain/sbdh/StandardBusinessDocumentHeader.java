//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.25 at 12:23:12 PM CET 
//


package no.difi.meldingsutveksling.domain.sbdh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import no.difi.meldingsutveksling.domain.MeldingsUtvekslingRuntimeException;
import no.difi.meldingsutveksling.domain.Organisasjonsnummer;
import no.difi.meldingsutveksling.validation.group.ValidationGroups;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


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
@Getter
@Setter
@ToString
public class StandardBusinessDocumentHeader {

    @XmlElement(name = "HeaderVersion", required = true)
    @NotNull
    private String headerVersion;

    @XmlElement(name = "Sender", required = true)
    @Size(max = 1)
    @Valid
    @ConvertGroup(to = ValidationGroups.Partner.Sender.class)
    private Set<@Valid Sender> sender;

    @XmlElement(name = "Receiver", required = true)
    @NotEmpty
    @Size(min = 1, max = 1)
    @Valid
    @ConvertGroup(to = ValidationGroups.Partner.Receiver.class)
    private Set<@Valid Receiver> receiver;

    @XmlElement(name = "DocumentIdentification", required = true)
    @NotNull
    @Valid
    private DocumentIdentification documentIdentification;

    @XmlElement(name = "Manifest")
    @Valid
    private Manifest manifest;

    @XmlElement(name = "BusinessScope")
    @NotNull
    @Valid
    private BusinessScope businessScope;

    public void setSender(Set<Sender> sender) {
        this.sender = sender;
    }

    public Set<Sender> getSender() {
        if (sender == null) {
            sender = new HashSet<>();
        }
        return this.sender;
    }

    public StandardBusinessDocumentHeader addSender(Sender partner) {
        getSender().add(partner);
        return this;
    }

    public Set<Receiver> getReceiver() {
        if (receiver == null) {
            receiver = new HashSet<>();
        }
        return this.receiver;
    }

    public StandardBusinessDocumentHeader addReceiver(Receiver partner) {
        getReceiver().add(partner);
        return this;
    }

    @JsonIgnore
    Optional<Sender> getFirstSender() {
        if (sender == null) {
            return Optional.empty();
        }
        return sender.stream().findFirst();
    }

    @JsonIgnore
    Optional<Receiver> getFirstReceiver() {
        if (receiver == null) {
            return Optional.empty();
        }
        return receiver.stream().findFirst();
    }

    @JsonIgnore
    public String getReceiverOrganisationNumber() {

        if (receiver.size() != 1) {
            throw new MeldingsUtvekslingRuntimeException(String.valueOf(receiver.size()));
        }
        Partner partner = receiver.iterator().next();
        PartnerIdentification identifier = partner.getIdentifier();
        if (identifier == null) {
            throw new MeldingsUtvekslingRuntimeException();
        }
        return identifier.getValue();
    }

    public static class Builder {

        private static final String HEADER_VERSION = "1.0";
        private static final String TYPE_VERSION = "2.0";

        private Organisasjonsnummer avsender;
        private Organisasjonsnummer mottaker;
        private String journalPostId;
        private String conversationId;
        private String messageId;
        private String type;
        private String documentType;
        private String process;

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

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder process(String process) {
            this.process = process;
            return this;
        }

        public Builder relatedToConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder relatedToMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public StandardBusinessDocumentHeader build() {
            StandardBusinessDocumentHeader sbdh = new StandardBusinessDocumentHeader()
                    .setHeaderVersion(HEADER_VERSION)
                    .addSender(createSender(avsender))
                    .addReceiver(createReciever(mottaker))
                    .setBusinessScope(createBusinessScope(fromConversationId(conversationId)))
                    .setDocumentIdentification(createDocumentIdentification(messageId, type, documentType));
            if (StringUtils.hasText(journalPostId)) {
                sbdh.getBusinessScope().getScope().add(fromJournalPostId(journalPostId));
            }
            return sbdh;
        }

        private Sender createSender(Organisasjonsnummer orgNummer) {
            return new Sender()
                    .setIdentifier(new PartnerIdentification()
                            .setValue(orgNummer.asIso6523())
                            .setAuthority(orgNummer.authority()));
        }

        private Receiver createReciever(Organisasjonsnummer orgNummer) {
            return new Receiver()
                    .setIdentifier(new PartnerIdentification()
                            .setValue(orgNummer.asIso6523())
                            .setAuthority(orgNummer.authority()));
        }

        private DocumentIdentification createDocumentIdentification(String messageId, String type, String documentType) {
            if (documentType == null) {
                throw new MeldingsUtvekslingRuntimeException("DocumentType must be set");
            }

            return new DocumentIdentification()
                    .setCreationDateAndTime(OffsetDateTime.now())
                    .setStandard(documentType)
                    .setType(type)
                    .setTypeVersion(TYPE_VERSION)
                    .setInstanceIdentifier(messageId);
        }

        private BusinessScope createBusinessScope(Scope... scopes) {
            return new BusinessScope()
                    .setScope(new HashSet<>(Arrays.asList(scopes)));
        }

        private Scope fromJournalPostId(String journalPostId) {
            return createDefaultScope()
                    .setType(ScopeType.JOURNALPOST_ID.toString())
                    .setInstanceIdentifier(journalPostId);
        }

        private Scope fromConversationId(String conversationId) {
            return createDefaultScope()
                    .setType(ScopeType.CONVERSATION_ID.toString())
                    .setInstanceIdentifier(conversationId);
        }

        private Scope createDefaultScope() {
            if (process == null) {
                throw new MeldingsUtvekslingRuntimeException("Process must be set");
            }

            return new Scope().setIdentifier(process);
        }
    }
}
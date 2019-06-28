package no.difi.meldingsutveksling.noarkexchange;

import net.logstash.logback.marker.LogstashMarker;
import no.difi.meldingsutveksling.ServiceIdentifier;
import no.difi.meldingsutveksling.config.IntegrasjonspunktProperties;
import no.difi.meldingsutveksling.logging.Audit;
import no.difi.meldingsutveksling.logging.MarkerFactory;
import no.difi.meldingsutveksling.nextmove.ConversationStrategyFactory;
import no.difi.meldingsutveksling.nextmove.v2.NextMoveMessageService;
import no.difi.meldingsutveksling.noarkexchange.schema.*;
import no.difi.meldingsutveksling.serviceregistry.ServiceRegistryLookup;
import no.difi.meldingsutveksling.serviceregistry.ServiceRegistryLookupException;
import no.difi.meldingsutveksling.serviceregistry.externalmodel.ServiceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static no.difi.meldingsutveksling.ServiceIdentifier.*;
import static no.difi.meldingsutveksling.noarkexchange.PutMessageMarker.markerFrom;

/**
 * This is the implementation of the wenbservice that case managenent systems supporting the BEST/EDU stadard communicates with.
 * The responsibility of this component is to create, sign and encrypt a SBD message for delivery to a PEPPOL access point
 * <p/>
 * The access point for the recipient is looked up through ELMA and SMK, the certificates are retrived through a MOCKED adress
 * register component not yet imolemented in any infrastructure.
 * <p/>
 * <p/>
 * User: glennbech Date: 31.10.14 Time: 15:26
 */
@org.springframework.stereotype.Component("noarkExchangeService")
@WebService(portName = "NoarkExchangePort", serviceName = "noarkExchange", targetNamespace = "http://www.arkivverket.no/Noark/Exchange", endpointInterface = "no.difi.meldingsutveksling.noarkexchange.schema.SOAPport")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class IntegrasjonspunktImpl implements SOAPport {

    private static final Logger log = LoggerFactory.getLogger(IntegrasjonspunktImpl.class);

    @Autowired
    private IntegrasjonspunktProperties properties;

    @Autowired
    private ServiceRegistryLookup serviceRegistryLookup;

    @Autowired
    private ConversationStrategyFactory strategyFactory;

    @Autowired
    private NextMoveMessageService messageService;

    @Override
    public GetCanReceiveMessageResponseType getCanReceiveMessage(@WebParam(name = "GetCanReceiveMessageRequest", targetNamespace = "http://www.arkivverket.no/Noark/Exchange/types", partName = "getCanReceiveMessageRequest") GetCanReceiveMessageRequestType getCanReceiveMessageRequest) {

        String orgnr = getCanReceiveMessageRequest.getReceiver().getOrgnr();
        GetCanReceiveMessageResponseType response = new GetCanReceiveMessageResponseType();

        Predicate<String> personnrPredicate = Pattern.compile(String.format("\\d{%d}", 11)).asPredicate();
        if (personnrPredicate.test(orgnr)) {
            response.setResult(false);
            return response;
        }

        final ServiceRecord serviceRecord;
        try {
            serviceRecord = serviceRegistryLookup.getServiceRecord(orgnr);
        } catch (Exception e) {
            log.error("Exception during service registry lookup: ", e);
            response.setResult(false);
            return response;
        }

        final LogstashMarker receiverMarker = MarkerFactory.receiverMarker(orgnr);

        if (asList(DPO, DPF, DPV).contains(serviceRecord.getServiceIdentifier()) &&
                strategyFactory.getStrategy(serviceRecord.getServiceIdentifier()).isPresent()) {
            Audit.info(String.format("CanReceive = true. Receiver = %s, service identifier = %s", orgnr, serviceRecord.getServiceIdentifier()), receiverMarker);
            response.setResult(true);
            return response;
        }

        Audit.error(String.format("CanReceive = false. Receiver (%s) accepts %s, but feature is disabled.", orgnr, serviceRecord.getServiceIdentifier()), receiverMarker);
        response.setResult(false);
        return response;
    }

    @Override
    public PutMessageResponseType putMessage(PutMessageRequestType request) {
        PutMessageRequestWrapper message = new PutMessageRequestWrapper(request);
        if (PayloadUtil.isAppReceipt(message.getPayload())) {
            if ("p360".equalsIgnoreCase(properties.getNoarkSystem().getType())) {
                message.swapSenderAndReceiver();
            }

            if (StringUtils.hasText(properties.getFiks().getInn().getFallbackSenderOrgNr()) &&
                    message.getReceiverPartyNumber().equals(properties.getFiks().getInn().getFallbackSenderOrgNr())) {
                Audit.info(String.format("Message is AppReceipt, but receiver (%s) is the configured fallback sender organization number. Discarding message.",
                        message.getReceiverPartyNumber()), markerFrom(message));
                return PutMessageResponseFactory.createOkResponse();
            }
        }

        ServiceRecord receiverRecord;
        try {
            receiverRecord = serviceRegistryLookup.getServiceRecord(message.getReceiverPartyNumber());
        } catch (ServiceRegistryLookupException e) {
            log.error("Error looking up service record for {}", message.getReceiverPartyNumber(), e);
            return PutMessageResponseFactory.createErrorResponse(StatusMessage.MISSING_SERVICE_RECORD);
        }

        if (PayloadUtil.isAppReceipt(message.getPayload()) &&
                receiverRecord.getServiceIdentifier() != ServiceIdentifier.DPO) {
            Audit.info(String.format("Message is AppReceipt, but receiver (%s) is not DPO. Discarding message.",
                    message.getReceiverPartyNumber()), markerFrom(message));
            return PutMessageResponseFactory.createOkResponse();
        }

        if (!message.hasSenderPartyNumber()) {
            message.setSenderPartyNumber(properties.getOrg().getNumber());
        }

        Audit.info(String.format("Received EDU message [id=%s]", message.getConversationId()), markerFrom(message));

        if (PayloadUtil.isEmpty(message.getPayload())) {
            Audit.error("Payload is missing", markerFrom(message));
            if (properties.getFeature().isReturnOkOnEmptyPayload()) {
                return PutMessageResponseFactory.createOkResponse();
            } else {
                return PutMessageResponseFactory.createErrorResponse(StatusMessage.MISSING_PAYLOAD);
            }
        }

        return messageService.convertAndSend(message);
    }

}

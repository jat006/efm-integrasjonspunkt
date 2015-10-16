package no.difi.meldingsutveksling.noark;

import com.thoughtworks.xstream.XStream;
import no.difi.meldingsutveksling.config.IntegrasjonspunktConfig;
import no.difi.meldingsutveksling.domain.ProcessState;
import no.difi.meldingsutveksling.eventlog.Event;
import no.difi.meldingsutveksling.eventlog.EventLog;
import no.difi.meldingsutveksling.noarkexchange.schema.*;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import java.io.IOException;

/**
 * @author Glenn Bech
 */
@Component
public class NoarkClientOld {

    @Autowired
    EventLog eventLog;

    @Autowired
    IntegrasjonspunktConfig config;

    public PutMessageResponseType sendEduMelding(PutMessageRequestType eduMesage) {
        if (eventLog == null) {
            throw new IllegalStateException("malconfigured. EventLog Not set");
        }

        if (eduMesage.getEnvelope() == null || eduMesage.getEnvelope().getReceiver() == null || eduMesage.getEnvelope().getSender() == null) {
            eventLog.log(Event.errorEvent("", "", ProcessState.MESSAGE_SEND_FAIL, "invalid envelope", new XStream().toXML(eduMesage)));
            throw new IllegalStateException("invalid envelope");
        }

        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("no.difi.meldingsutveksling.noarkexchange.p360.schema");
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
        ModelMapper mapper = new ModelMapper();

        no.difi.meldingsutveksling.noarkexchange.p360.schema.PutMessageRequestType p360request =
                new no.difi.meldingsutveksling.noarkexchange.p360.schema.PutMessageRequestType();

        mapper.map(eduMesage, p360request);
        JAXBElement<no.difi.meldingsutveksling.noarkexchange.p360.schema.PutMessageRequestType> request
                = new no.difi.meldingsutveksling.noarkexchange.p360.schema.ObjectFactory().createPutMessageRequest(p360request);

        if (isAuthenticationEnabled() && isNTLMAuthentication()) {
            HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
            HttpClient httpClient = HttpClientBuilder.create()
                    .addInterceptorFirst(new PreemtiveNTLMRequestInterceptor()).build();
            httpComponentsMessageSender.setHttpClient(httpClient);
            webServiceTemplate.setMessageSender(httpComponentsMessageSender);
        }

        JAXBElement<no.difi.meldingsutveksling.noarkexchange.p360.schema.PutMessageResponseType> response
                = (JAXBElement) webServiceTemplate.marshalSendAndReceive(config.getLocalNoarkClientSettings().getEndpointUrl(), request,
                new SoapActionCallback("http://www.arkivverket.no/Noark/Exchange/IEDUImport/PutMessage"));

        PutMessageResponseType theResponse = new PutMessageResponseType();
        mapper.map(response.getValue(), theResponse);

        return theResponse;
    }

    private boolean isNTLMAuthentication() {
        IntegrasjonspunktConfig.NoarkClientSettings localNoarkClientSettings = config.getLocalNoarkClientSettings();
        return localNoarkClientSettings.getDomain() != null && !localNoarkClientSettings.getDomain().isEmpty();
    }

    private boolean isAuthenticationEnabled() {
        IntegrasjonspunktConfig.NoarkClientSettings localNoarkClientSettings = config.getLocalNoarkClientSettings();
        return localNoarkClientSettings.getUserName() != null && !localNoarkClientSettings.getUserName().isEmpty();
    }

    private SOAPport getSoapPport() {
        NoarkExchange exchange = new NoarkExchange();
        SOAPport port = exchange.getNoarkExchangePort();
        BindingProvider bp = (BindingProvider) port;
        String endPointURL = config.getLocalNoarkClientSettings().getEndpointUrl();
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointURL);
        return port;
    }

    public EventLog getEventLog() {
        return eventLog;
    }

    public void setEventLog(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public boolean canGetRecieveMessage(String orgnr) {
        GetCanReceiveMessageRequestType req = new GetCanReceiveMessageRequestType();
        AddressType addressType = new AddressType();
        addressType.setOrgnr(orgnr);
        req.setReceiver(addressType);
        GetCanReceiveMessageResponseType responseType = getSoapPport().getCanReceiveMessage(req);
        return responseType.isResult();
    }

    private class PreemtiveNTLMRequestInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            if (httpRequest.containsHeader(HTTP.CONTENT_LEN)) {
                httpRequest.removeHeaders(HTTP.CONTENT_LEN);
            }

            IntegrasjonspunktConfig.NoarkClientSettings settings = config.getLocalNoarkClientSettings();
            AuthState authState = (AuthState) httpContext.getAttribute(HttpClientContext.TARGET_AUTH_STATE);
            if (authState.getAuthScheme() == null) {
                AuthScheme ntlmScheme = new NTLMScheme();
                Credentials creds = new NTCredentials(settings.getUserName(), settings.getPassword(), null, settings.getDomain());
                authState.update(ntlmScheme, creds);
            }
        }
    }
}

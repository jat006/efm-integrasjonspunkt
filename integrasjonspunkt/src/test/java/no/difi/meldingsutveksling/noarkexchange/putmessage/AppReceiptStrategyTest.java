package no.difi.meldingsutveksling.noarkexchange.putmessage;

import no.difi.meldingsutveksling.eventlog.EventLog;
import no.difi.meldingsutveksling.noarkexchange.MessageSender;
import no.difi.meldingsutveksling.noarkexchange.schema.AddressType;
import no.difi.meldingsutveksling.noarkexchange.schema.EnvelopeType;
import no.difi.meldingsutveksling.noarkexchange.schema.PutMessageRequestType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Test for the AppReceiptStrategy
 */
public class AppReceiptStrategyTest {

    public static final String RECEIVER_ORG_NR = "11111111";
    public static final String SENDER_ORG_NR = "22222222";
    private String receiptPayload = "<AppReceipt type=\"OK\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.arkivverket.no/Noark/Exchange/types\">\n" +
            "  <message code=\"ID\" xmlns=\"\">\n" +
            "    <text>210725</text>\n" +
            "  </message>\n" +
            "</AppReceipt>";

    private PutMessageContext ctx;

    @Before
    public void init() {
        ctx = new PutMessageContext(Mockito.mock(EventLog.class), Mockito.mock(MessageSender.class));
    }

    /**
     * The AppReceiptStrategy should send the receipt back to the sender
     */
    @Test
    public void appReceiptsShouldBeReturnedToSender() {
        AppReceiptPutMessageStrategy strategy = new AppReceiptPutMessageStrategy(ctx.getMessageSender());
        PutMessageRequestType request = createPutMessageRequestType();
        strategy.putMessage(request);
        verify(ctx.getMessageSender(), atLeastOnce()).sendMessage(any(PutMessageRequestType.class));
    }

    private PutMessageRequestType createPutMessageRequestType() {
        PutMessageRequestType request = new PutMessageRequestType();
        final EnvelopeType envelope = new EnvelopeType();
        AddressType receiver = new AddressType();
        receiver.setOrgnr(RECEIVER_ORG_NR);
        envelope.setReceiver(receiver);

        AddressType sender = new AddressType();
        sender.setOrgnr(SENDER_ORG_NR);
        envelope.setSender(sender);

        request.setEnvelope(envelope);
        request.setPayload(receiptPayload);
        return request;
    }
}

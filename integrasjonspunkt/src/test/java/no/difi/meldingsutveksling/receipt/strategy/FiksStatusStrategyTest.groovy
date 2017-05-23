package no.difi.meldingsutveksling.receipt.strategy

import no.difi.meldingsutveksling.ks.SvarUtService
import no.difi.meldingsutveksling.ks.receipt.DpfReceiptStatus
import no.difi.meldingsutveksling.receipt.Conversation
import no.difi.meldingsutveksling.receipt.ConversationRepository
import no.difi.meldingsutveksling.receipt.MessageStatus
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.time.LocalDateTime

import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

@RunWith(JUnit4)
class FiksStatusStrategyTest {
    private SvarUtService service

    @Before
    void setup() {
        service = mock(SvarUtService)

    }

    @Test
    void "given message receipt with status read then conversation should be set non pollable"() {
        def messageReceipt = MessageStatus.of(DpfReceiptStatus.LEST.toString(), LocalDateTime.now())
        Conversation conversation = mock(Conversation)
        FiksStatusStrategy strategy = new FiksStatusStrategy(service, mock(ConversationRepository))
        when(service.getMessageReceipt(any(Conversation))).thenReturn(messageReceipt)

        strategy.checkStatus(conversation)

        verify(conversation).setPollable(false)
    }
}
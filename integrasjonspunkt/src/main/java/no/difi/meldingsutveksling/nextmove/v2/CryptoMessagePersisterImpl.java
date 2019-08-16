package no.difi.meldingsutveksling.nextmove.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.difi.meldingsutveksling.IntegrasjonspunktNokkel;
import no.difi.meldingsutveksling.dokumentpakking.service.CmsUtil;
import no.difi.meldingsutveksling.nextmove.message.CryptoMessagePersister;
import no.difi.meldingsutveksling.nextmove.message.FileEntryStream;
import no.difi.meldingsutveksling.nextmove.message.MessagePersister;
import no.difi.meldingsutveksling.pipes.Pipe;
import no.difi.meldingsutveksling.pipes.Plumber;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import static no.difi.meldingsutveksling.pipes.PipeOperations.close;
import static no.difi.meldingsutveksling.pipes.PipeOperations.copy;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoMessagePersisterImpl implements CryptoMessagePersister {

    private final MessagePersister delegate;
    private final ObjectProvider<CmsUtil> cmsUtilProvider;
    private final IntegrasjonspunktNokkel keyInfo;
    private final Plumber plumber;

    public void write(String messageId, String filename, byte[] message) throws IOException {
        byte[] encryptedMessage = getCmsUtil().createCMS(message, keyInfo.getX509Certificate());
        delegate.write(messageId, filename, encryptedMessage);
    }

    public void writeStream(String messageId, String filename, InputStream stream, long size) throws IOException {
        Pipe pipe = plumber.pipe("CMS encrypt", inlet -> cmsUtilProvider.getIfAvailable().createCMSStreamed(stream, inlet, keyInfo.getX509Certificate()));
        try (PipedInputStream is = pipe.outlet()) {
            delegate.writeStream(messageId, filename, is, size);
        }
    }

    public byte[] read(String messageId, String filename) throws IOException {
        return getCmsUtil().decryptCMS(delegate.read(messageId, filename), keyInfo.loadPrivateKey());
    }

    public FileEntryStream readStream(String messageId, String filename) {
        InputStream inputStream = delegate.readStream(messageId, filename).getInputStream();
        PipedInputStream pipedInputStream = plumber.pipe("Reading file", copy(inputStream).andThen(close(inputStream))).outlet();
        return FileEntryStream.of(getCmsUtil().decryptCMSStreamed(pipedInputStream, keyInfo.loadPrivateKey()), -1);
    }

    public void delete(String messageId) throws IOException {
        delegate.delete(messageId);
    }

    private CmsUtil getCmsUtil() {
        return cmsUtilProvider.getIfAvailable();
    }
}
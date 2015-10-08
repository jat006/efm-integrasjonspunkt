package no.difi.meldingsutveksling.adresseregister.client;

import no.difi.meldingsutveksling.domain.MeldingsUtvekslingRuntimeException;

/**
 * @author Glenn Bech
 */
public class CertificateNotFoundException extends MeldingsUtvekslingRuntimeException {
    public CertificateNotFoundException(RuntimeException cause) {
        super(cause);
    }
}

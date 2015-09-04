package no.difi.meldingsutveksling.integrasjonspunkt.altinnreceive;

/**
 * Wrapper class to hold configuration options used in this program
 *
 * @author Glenn Bech
 */
public class AltinnBatchImportConfiguration {

    private String integrasjonspunktEndPointURL;
    private String organisationNumber;
    private int threadPoolSize;

    public AltinnBatchImportConfiguration(String integrasjonspunktEndPointURL, String organisationNumber, int threadPoolSize) {
        this.integrasjonspunktEndPointURL = integrasjonspunktEndPointURL;
        this.organisationNumber = organisationNumber;
        this.threadPoolSize = threadPoolSize;
    }

    public String getIntegrasjonspunktEndPointURL() {
        return integrasjonspunktEndPointURL;
    }

    public String getOrganisationNumber() {
        return organisationNumber;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}

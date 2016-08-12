package no.difi.meldingsutveksling.serviceregistry;

import com.google.gson.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import no.difi.meldingsutveksling.serviceregistry.client.RestClient;
import no.difi.meldingsutveksling.serviceregistry.externalmodel.ServiceRecord;

public class ServiceRegistryLookup {
    private final RestClient client;

    public ServiceRegistryLookup(RestClient client) {
        this.client = client;
    }

    /**
     * Method to find out which transport channel to use to send messages to given organization
     * @param orgnumber organization number of the receiver
     * @return a ServiceRecord if a primary service record could be determined. Otherwise an empty ServiceRecord is
     * returned.
     */
    public ServiceRecord getPrimaryServiceRecord(String orgnumber) {
        final String serviceRecords = client.getResource("organization/" + orgnumber);
        final DocumentContext documentContext = JsonPath.parse(serviceRecords, jsonPathConfiguration());
        if (getNumberOfServiceRecords(documentContext) == 1) {
            return documentContext.read("$.serviceRecords[0].serviceRecord", ServiceRecord.class);
        }

        final JsonElement primaryServiceIdentifier = documentContext.read("$.infoRecord.primaryServiceIdentifier");
        if (primaryServiceIdentifier instanceof JsonNull) {
            return ServiceRecord.EMPTY;
        } else {
            final JsonArray res = documentContext.read("$.serviceRecords[?(@.serviceRecord.serviceIdentifier == $.infoRecord.primaryServiceIdentifier)].serviceRecord");
            return new Gson().fromJson(res.get(0), ServiceRecord.class);
        }

    }

    private int getNumberOfServiceRecords(DocumentContext documentContext) {
        return documentContext.read("$.serviceRecords.length()");
    }

    private Configuration jsonPathConfiguration() {
        final Gson gson = new GsonBuilder().serializeNulls().create();
        return Configuration.defaultConfiguration().jsonProvider(new GsonJsonProvider(gson));
    }
}
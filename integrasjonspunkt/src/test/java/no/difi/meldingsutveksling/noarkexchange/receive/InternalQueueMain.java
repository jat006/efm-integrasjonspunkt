package no.difi.meldingsutveksling.noarkexchange.receive;

import no.difi.meldingsutveksling.config.JmsConfiguration;
import no.difi.meldingsutveksling.domain.sbdh.Document;
import no.difi.meldingsutveksling.domain.sbdh.StandardBusinessDocumentHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * This class can be used to explore the JMS queue.
 */
@SpringBootApplication(exclude = {SolrAutoConfiguration.class, FlywayAutoConfiguration.class})
public class InternalQueueMain extends SpringBootServletInitializer {

    @Autowired
    InternalQueue queue;

    public void testPut() {
        Document document = new Document();
        StandardBusinessDocumentHeader header = new StandardBusinessDocumentHeader();
        header.setHeaderVersion("some header version");
        document.setStandardBusinessDocumentHeader(header);
        queue.put(document);
    }

    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(new Object[]{InternalQueueMain.class, JmsConfiguration.class}, args);
        InternalQueueMain bean = context.getBean(InternalQueueMain.class);
        bean.testPut();
    }
}
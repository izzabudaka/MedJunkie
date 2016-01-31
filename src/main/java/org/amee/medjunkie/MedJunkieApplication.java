package org.amee.medjunkie;

import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.amee.medjunkie.health.TemplateHealthCheck;
import org.amee.medjunkie.model.Book;
import org.amee.medjunkie.persistence.BookStore;
import org.amee.medjunkie.resource.BookResource;
import org.amee.medjunkie.resource.BooksResource;
import org.amee.medjunkie.resource.SearchResource;
import org.amee.medjunkie.service.ElasticSearchService;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by tuffle on 05/09/2015.
 */
public class MedJunkieApplication extends Application<MedJunkieConfiguration> {
    public static void main(String[] args) throws Exception {
        new MedJunkieApplication().run(args);
    }

    @Override
    public String getName() {
        return "Simple MedJunkie Prototype";
    }

    public Client client;

    @Override
    public void initialize(Bootstrap<MedJunkieConfiguration> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());

        this.client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(ElasticSearchService.SERVER, 9300));
    }

    @Override
    public void run(MedJunkieConfiguration configuration, Environment environment) {
        final BookStore bookStore = BookStore.getInstance();
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(new BookResource(bookStore));
        environment.jersey().register(new BooksResource(bookStore));
        environment.jersey().register(new SearchResource(client));

        bookStore.add(new Book("testBook"));
    }
}

package org.amee.medjunkie.service;

import com.google.gson.Gson;
import org.amee.medjunkie.model.Figure;
import org.amee.medjunkie.model.Hint;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Home on 05/09/15.
 */
public class ElasticSearchService {


    public static final String SERVER = "192.168.0.125";//"http://ec2-52-17-185-167.eu-west-1.compute.amazonaws.com";
    public static final String CLUSTER_NAME = "elasticsearch";
    private final Client client;

    public static void main(String[] args) {
        ElasticSearchService es = new ElasticSearchService();

        Hint hint = Hint.newInstance("dummyChapter", "dummysection", "This is text");

        es.createHintIndex(hint, es.client);

        Map result = es.getHintIndex(hint, es.client);

        System.out.println("We have returned\n" + result);
                        /*
        System.out.println("Current results: " + results.length);
        for (SearchHit hit : results) {
            System.out.println("------------------------------");
            Map<String,Object> result = hit.getSource();
            System.out.println(result);
        }
        */
    }

    public ElasticSearchService() {

//        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();

  //      Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(SERVER, 9300));

        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(SERVER, 9300));

        this.client = client;
    }

    public Client getClient(){
        return client;
    }

    public String getJson(Object hint) {
        return (new Gson().toJson(hint));
    }

    public void createBulkRequest(Client client, ArrayList hints) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for( int i = 0; i < hints.size(); i++ ) {

            bulkRequest.add(client.prepareIndex("hints", "hint")
                            .setSource(getJson(hints.get(i)))
            );
        }
        if (hints.size() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println("Bulk Request Failed.");
            }
        }
    }
    public void createBulkRequestSymptom(Client client, ArrayList symp) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for( int i = 0; i < symp.size(); i++ ) {

            bulkRequest.add(client.prepareIndex("symptoms", "symptom")
                            .setSource(getJson(symp.get(i)))
            );
        }
        if (symp.size() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println("Bulk Request Failed.");
            }
        }
    }
    public void createBulkRequestImage(Client client, ArrayList<Figure> figures) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for( int i = 0; i < figures.size(); i++ ) {

            bulkRequest.add(client.prepareIndex("images", "image")
                            .setSource(getJson(figures.get(i)))
            );
        }

        if(figures.size() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println("Bulk Request Failed.");
            }
        }

    }


    public void createHintIndex(Hint hint, Client client) {
        String jsonDoc = getJson(hint);

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("hints");
        createIndexRequestBuilder.execute().actionGet();

//        client.prepareIndex(hint.getSectionTitle(), "hint", Long.toString(hint.getId()))
//                .setSource(jsonDoc)
//                .execute()
//                .actionGet();


    }

    public Map getHintIndex(Hint hint, Client client) {
        GetResponse getResponse = client.prepareGet("hints", "hint", Long.toString(hint.getId())).execute().actionGet();

        Map<String, Object> source = getResponse.getSource();

        return (source);
    }

    // searchDocument(client, "kodcucom", "article", "title", "ElasticSearch");
    public SearchHit[] searchDocument(Client client, String index, String type,
                                      String field, String value) {

        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.termQuery(field, value))
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        SearchHit[] results = response.getHits().getHits();

        /*
        System.out.println("Current results: " + results.length);
        for (SearchHit hit : results) {
            System.out.println("------------------------------");
            Map<String,Object> result = hit.getSource();
            System.out.println(result);
        }
        */
        return (results);

    }
}

package org.amee.medjunkie.resource;

import org.amee.medjunkie.model.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by tuffle on 05/09/2015.
 */

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksResource.class);

    public Client elasticClient;

    public SearchResource(Client elasticClient) {
        this.elasticClient = elasticClient;
    }

    @POST
    public Response searchElastic(Search search) {
        String category = search.getCategoryType().name().toLowerCase();

        HashMap<String, SearchHit[]> stuff = new HashMap<String, SearchHit[]>();

        for(String type: Category.CategoryType.names()) {
            String lowCase = type.toLowerCase();
            SearchHit[] hits = query(search.getSearchTerms(), lowCase);
            stuff.put(lowCase, hits);
        }


        FinalResult fres = new FinalResult();

        for(Map.Entry entry: stuff.entrySet()) {
            String type      = (String) entry.getKey();
            SearchHit[] hits = (SearchHit[]) entry.getValue();
            ArrayList<Category> items = new ArrayList<Category>();
            resolveData(type, hits, items);

            switch (type) {
                case "hints":
                    Category stuffD[] = new Category[items.size()];
                    fres.category = getAllTokens(items.toArray(stuffD));

                    break;

                case "questions":
                    fres.questions = items;
                    break;

                default:
                    break;
            }


        }

        return Response.ok().entity(fres).build();

    }

    public void resolveData(String category,
                            SearchHit[] hits,
                            ArrayList<Category> list)
    {
        for(SearchHit hit: hits) {
            Map<String, Object> values = hit.sourceAsMap();

            String sectionTitle = (String) values.get("sectionTitle");
            String text;

            switch (category) {
                case "images":
                    text = (String) values.get("link");
                    list.add(Figure.newInstance("Chapter", sectionTitle, text));
                    break;
                case "questions":
                    text = (String) values.get("question");



                    List<HashMap<String, Object>> options = (ArrayList<HashMap<String,Object>>) values.get("options");

                    List<MultipleChoiceOption> multiOptions = new ArrayList<>();
                    for(HashMap<String, Object> option : options) {
                        multiOptions.add(new MultipleChoiceOption((String)option.get("option"), (Boolean)option.get("answer")));
                    }


                    list.add(Question.newInstance(text, multiOptions));
                    break;
                case "hints":
                    text = (String) values.get("text");
                    list.add(Hint.newInstance("Chapter", sectionTitle, text));
                    break;
                case "symptoms":
                    text = (String) values.get("text");
                    list.add(Symptoms.newInstance("Chapter", sectionTitle, text));
                    break;
                default:

            }


        }
    }

    public Category getAllTokens(Category[] result) {
        List<String> arrayList = new ArrayList<>();

        for(Category res : result)
            arrayList.addAll(Arrays.asList(res.getTextTokens()));

        String[] allTokens = new String[arrayList.size()];

        Hint allHint = Hint.newInstance("ALL_RECORDS", "ALL_RECORDS", "ALL_TEXT");
        allHint.setTextTokens(arrayList.toArray(allTokens));

        return allHint;
    }

    public SearchHit[] query(String terms, String category) {
        QueryBuilder qb = queryStringQuery(terms);

        SearchResponse response = this.elasticClient.prepareSearch(category)
                .setTypes(category.substring(0, category.length() - 1))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .execute()
                .actionGet();

        return response.getHits().getHits();
    }

}

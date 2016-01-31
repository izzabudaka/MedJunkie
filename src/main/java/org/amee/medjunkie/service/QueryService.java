package org.amee.medjunkie.service;

import com.google.gson.Gson;
import org.amee.medjunkie.model.Book;
import org.amee.medjunkie.model.Hint;
import org.elasticsearch.client.Client;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tuffle on 05/09/2015.
 */
public class QueryService {

    public enum RawData {
        BOOKSMETA("/Edata"),
        BOOKS("/data/books/"),
        QUESTIONS_ENDO("/data/questions/endocrine/"),
        QUESTIONS_HAEMO("/data/questions/haemotology-immune"),
        ASSETS("/data/assets/");

        private String path;

        RawData(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static void main(String[] args) {
        try{
            File[] xmlFiles = getFiles(RawData.BOOKS);
            ElasticSearchService elastic = new ElasticSearchService();

            ArrayList<Book> books = new ArrayList<>();
            books = BookQueryService.getBooks();

            // CREATING hints
            for(Book book : books) {
                elastic.createBulkRequest(elastic.getClient(), book.getHints());
                elastic.createBulkRequest(elastic.getClient(), book.getSyms());
                elastic.createBulkRequest(elastic.getClient(), book.getImages());
            }

            // Creating Questions
            File basePath = new File(System.getProperty("user.dir") + RawData.QUESTIONS_ENDO.getPath());

            File fileQ  = new File(basePath + "/imsmanifest.xml");
            QuestionQueryService.getQuestions(fileQ);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static File[] getFiles(RawData dataPath) {
        File dir = new File(System.getProperty("user.dir") + dataPath.getPath());
        File[] xmlFiles = dir.listFiles((folder, name) -> {
            return name.toLowerCase().endsWith(".xml");
        });

        return xmlFiles;
    }


}

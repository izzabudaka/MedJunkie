package org.amee.medjunkie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.amee.medjunkie.model.Hint;
import org.amee.medjunkie.model.MultipleChoiceOption;
import org.amee.medjunkie.model.Question;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.amee.medjunkie.service.QueryService.*;
import static org.amee.medjunkie.service.QueryService.RawData.*;

/**
 * Created by tuffle on 05/09/2015.
 */
public class QuestionQueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionQueryService.class);

    /**
     * Import All Questions
     * @param inputFile
     * @return
     */
    public static void getQuestions(File inputFile) {
        ArrayList hints = new ArrayList();

        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(ElasticSearchService.SERVER, 9300));

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        try {
            Document doc = getDocByPath(inputFile);

//            hints = new ArrayList();
//
            NodeList nList = null;//doc.getElementsByTagName("manifest");

            XPath xpath = XPathFactory.newInstance().newXPath();

            String expr = "/manifest/resources/resource";

            NodeList nodeList = (NodeList)xpath.compile(expr).evaluate(doc, XPathConstants.NODESET);


            for (int j = 0; j < nodeList.getLength(); j++) {
                Element eNode = (Element) nodeList.item(j);

                String href  = eNode.getAttribute("href");
                String ident = eNode.getAttribute("identifier");

                String filePath = System.getProperty("user.dir")
                                    + RawData.QUESTIONS_ENDO.getPath()
                                    + href;

                File fileX = new File(filePath);


                Document docX;

                try {
                    docX = getDocByPath(fileX);
                } catch(Exception e) {
                    continue;
                }


                String correctPath = "/assessmentItem/responseDeclaration/correctResponse/value";
                String answers     = "/assessmentItem/itemBody/choiceInteraction/simpleChoice";
                String body        = "/assessmentItem/itemBody/choiceInteraction/prompt";

                NodeList choice     = (NodeList)xpath.compile(answers).evaluate(docX, XPathConstants.NODESET);
                NodeList promt      = (NodeList)xpath.compile(body).evaluate(docX, XPathConstants.NODESET);
                NodeList correct    = (NodeList)xpath.compile(correctPath).evaluate(docX, XPathConstants.NODESET);


                String correctId = ((Element) correct.item(0)).getTextContent();
                String promtBody = ((Element) promt.item(0)).getTextContent().replace("\n", "").trim();

                List<MultipleChoiceOption> options = new ArrayList<MultipleChoiceOption>();

                if(choice.getLength() > 5) { continue; }

                for (int i = 0; i < choice.getLength() ; i++) {

                    Element xx = (Element) choice.item(i);

                    String questionBody = xx.getTextContent().replace("\n", "").replaceAll("\\s+", " ").trim();
                    boolean valid = false;

                    if(xx.getAttribute("identifier").compareTo(correctId) == 0) {
                        valid = true;
                    }

                    MultipleChoiceOption opt = new MultipleChoiceOption(questionBody, valid);

                    options.add(opt);

                }

                Question q = new Question(promtBody, options);

                ObjectMapper op = new ObjectMapper();
                String json = op.writeValueAsString(q);

                bulkRequest.add(client.prepareIndex("questions", "question").setSource(json));

            }

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if (bulkResponse.hasFailures()) {
                System.out.println("Bulk Request Failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Document getDocByPath(File inputFile) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        return doc;

    }
}

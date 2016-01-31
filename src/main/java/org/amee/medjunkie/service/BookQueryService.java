package org.amee.medjunkie.service;

import org.amee.medjunkie.model.Book;
import org.amee.medjunkie.model.Hint;
import org.amee.medjunkie.model.Figure;
import org.amee.medjunkie.model.Symptoms;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

import static org.amee.medjunkie.service.QueryService.*;

public class BookQueryService {

    public static void main(String[] args){

        ArrayList<Book> books = getBooks();
    }

    public static ArrayList<Book> getBooks() {

        ArrayList<Book> bookItems = new ArrayList<>();

        try{
            File books = new File(System.getProperty("user.dir") + RawData.BOOKSMETA.getPath());
            for (final File bookFile : books.listFiles()) {

                String title = "";
                ArrayList hints = new ArrayList();
                ArrayList syms = new ArrayList();
                ArrayList imgs = new ArrayList();

                if (bookFile.isDirectory()) {
                    for( final File bookData : bookFile.listFiles()) {
                        if (!bookData.isDirectory()) {
                            title = getTitle(new File(bookData.getPath()));
                        }
                        else{
                            File bookBody = new File(bookData.getPath() + "/BODY/");
                            for ( final File chapter : bookBody.listFiles()){
                                for( final File chapterPart : chapter.listFiles()) {
                                  if(!chapterPart.isDirectory() && chapterPart.getName().endsWith("main.xml")){
                                      File xmlFile = new File(chapterPart.getPath());
                                      hints = getHint(xmlFile, title);
                                      syms = getSymptoms(xmlFile, title);
                                      imgs = getFigures(xmlFile, title);
                                  }
                                }
                            }
                        }
                    }
                }
                bookItems.add(new Book(title, hints, syms, imgs));
            }
            return bookItems;
            }
            catch (Exception e) {

        }
        return new ArrayList<>();
    }
    public static String getTitle(File dataFile){

        String title = "";

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(dataFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("working-title");

            title = nList.item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
            return (title);
        }

        return title;
    }
    public static ArrayList getSymptoms(File inputFile, String bookTitle){
        ArrayList symptoms = new ArrayList();

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            symptoms = new ArrayList();

            NodeList nList = doc.getElementsByTagName("ce:section");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                String title = eElement.getElementsByTagName("ce:section-title").item(0).getTextContent();

                NodeList symXML = eElement.getElementsByTagName("ce:list-item");

                for (int j = 0; j < symXML.getLength(); j++) {
                    Node hintNode = symXML.item(j);
                    Element hElement = (Element) hintNode;
                    NodeList paragraph;
                    try {
                        paragraph = hElement.getElementsByTagName("ce:para");
                        Symptoms symp = Symptoms.newInstance(bookTitle, title, paragraph.item(0).getTextContent().replace("\n", "").replaceAll("\\s+", " ").trim());
                        symptoms.add(symp);
                    } catch (Exception e) {
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return (symptoms);
        }

        return (symptoms);
    }
    public static ArrayList getHint(File inputFile, String bookTitle) {
        HashSet<Hint> hints = new HashSet<>();

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("ce:section");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                String title = eElement.getElementsByTagName("ce:section-title").item(0).getTextContent();

                NodeList hintXML = eElement.getElementsByTagName("ce:textbox");

                for (int j = 0; j < hintXML.getLength(); j++) {
                    Node hintNode = hintXML.item(j);
                    Element hElement = (Element) hintNode;
                    NodeList paragraph;
                    try {
                        paragraph = hElement.getElementsByTagName("ce:para");
                        Hint ahint = Hint.newInstance(bookTitle, title, paragraph.item(0).getTextContent().replace("\n", "").replaceAll("\\s+", " ").trim());
                        hints.add(ahint);
                    } catch (Exception e) {
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return (new ArrayList(hints));
        }

        return (new ArrayList(hints));
    }


    public static ArrayList getFigures(File inputFile, String bookTitle) {
        ArrayList figures = new ArrayList();

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            figures = new ArrayList();

            NodeList nList = doc.getElementsByTagName("ce:figure");
            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                NodeList titles = eElement.getElementsByTagName("ce:caption");
                String title = "";
                try {
                    if (titles != null) titles.item(0).getTextContent();

                    else break;
                } catch (Exception e)
                {

                }
                String figureLink = eElement.getElementsByTagName("ce:link").item(0)
                                    .getAttributes().getNamedItem("locator").getTextContent();

                Figure afigure = Figure.newInstance(bookTitle, title.trim(), figureLink);
                figures.add(afigure);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return(figures);
        }

        return (figures);
    }

}
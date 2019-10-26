/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import entity.Feed;
import entity.Image;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.FeedLogic;
import logic.ImageLogic;
import scraper.Post;
import scraper.Scraper;
import scraper.Sort;

/**
 *
 * @author Lanre
 */
@WebServlet(name = "ImageView", urlPatterns = {"/ImageView"})
public class ImageView extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ImageLogic imagelogic = new ImageLogic();

            List<Image> entity = imagelogic.getAll();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImageDelivery</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div display=\"inline-block\" justify-content=\"center\" >");
            for (Image e : entity) {
                out.printf("<img width=\"300\" height=\"200\" "
                        + "src=\"image/%s\">", FileUtility.getFileName(e.getPath()));

            }
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        FileUtility.createDirectory(System.getProperty("user.home") + "/My Documents/Reddit Images/");
        ImageLogic imagelogic = new ImageLogic();
        FeedLogic feedlogic = new FeedLogic();

        Consumer<Post> saveImage = (Post post) -> {
            //if post is an image and SFW
            if (post.isImage() && !post.isOver18()) {
                //get the path for the image which is unique
                String path = post.getUrl();
                //save it in img directory
                FileUtility.downloadAndSaveFile(path, System.getProperty("user.home") + "/My Documents/Reddit Images/");

                Map<String, String[]> map = new HashMap<>();
                map.put(ImageLogic.NAME, new String[]{post.getTitle()});
                map.put(ImageLogic.PATH, new String[]{post.getUrl()});
                map.put(ImageLogic.DATE, new String[]{Long.toString(post.getDate().getTime())});

                if (imagelogic.getImagesWithPath(post.getUrl()) == null) {
                    Image savedImage = imagelogic.createEntity(map);
                    Feed savedfeed = feedlogic.getWithId(4);
                    savedImage.setFeedid(savedfeed);
                    imagelogic.add(savedImage);
                }
            }
        };

        //create a new scraper
        Scraper scrap = new Scraper();
        //authenticate and set up a page for wallpaper subreddit with 5 posts soreted by HOT order
        scrap.authenticate().buildRedditPagesConfig("Wallpaper", 5, Sort.HOT);
        //get the next page 3 times and save the images.
        scrap.requestNextPage().proccessNextPage(saveImage);
 
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
    @Override
    public String getServletInfo() {
        return "Short description";
    } 

}

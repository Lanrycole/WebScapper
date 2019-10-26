package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Feed;
import entity.Host;
import javax.validation.ValidationException;
import logic.FeedLogic;
import logic.HostLogic;

/**
 *
 * @author Shariar
 */
@WebServlet(name = "CreateFeed", urlPatterns = {"/CreateFeed"})
public class CreateFeed extends HttpServlet {

    private String errorMessage = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Feed</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\" >");
            out.println("<div style=\"display: inline-block; text-align: left;\"> Create Feed");
            out.println("<form method=\"post\">");
             
            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            out.printf("HOST: <input type=\"text\" name=\"%s\" value=\"\"><br>", FeedLogic.HOST_ID);
            out.printf("PATH: <input type=\"text\" name=\"%s\" value=\"\"><br>", FeedLogic.PATH);
            out.printf("TYPE: <input type=\"text\" name=\"%s\" value=\"\"><br>", FeedLogic.TYPE);
            out.printf("USER: <input type=\"text\" name=\"%s\" value=\"\"><br>", FeedLogic.NAME);

            out.println("<br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add Feed and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
                 errorMessage="";
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * get method is called first when requesting a URL. since this servlet will
     * create a host this method simple delivers the html code. creation will be
     * done in doPost method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * this method will handle the creation of entity. as it is called by user
     * submitting data through browser.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        //create logic

        FeedLogic feedlogic = new FeedLogic();
        //grab the parameter NAME first
        String name = request.getParameter(FeedLogic.NAME);
        //since it is unique we check for duplicates.
        try {

            if (feedlogic.getFeedsWithName(name).isEmpty()) {
                //if no duplicates create the entity and add it.
                Feed savedfeed = feedlogic.createEntity(request.getParameterMap());
                HostLogic hostlogic = new HostLogic();
                Host savedhost = hostlogic.getWithId(Integer.parseInt(request.getParameter(FeedLogic.HOST_ID)));
                savedfeed.setHostid(savedhost);
                feedlogic.add(savedfeed);
            } else {
                //if duplicate print the error message
                errorMessage = "Host: \"" + name + "\" already exists";
            }
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("FeedTable");
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create an Account Entity";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}

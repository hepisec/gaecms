package de.hepisec.gaecms.servlet;

import de.hepisec.gaecms.controller.MediaFileController;
import de.hepisec.gaecms.Pager;
import static de.hepisec.gaecms.TypeUtil.parseInt;
import de.hepisec.gaecms.model.MediaFile;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hendrik Pilz
 */
public class MediaLibrary extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int limit = parseInt(getServletContext().getInitParameter("mediaFilesPerPage"), 12);
                
        MediaFileController controller = new MediaFileController(getServletContext());
        Pager<MediaFile> pager = new Pager<>(controller, limit);
        pager.apply(request);
        
        if ("true".equals(request.getParameter("embedded"))) {
            request.getRequestDispatcher("/admin/media_library_embedded.jsp").forward(request, response);            
        } else {
            request.getRequestDispatcher("/admin/media_library.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

package de.hepisec.gaecms.servlet;

import de.hepisec.gaecms.controller.MediaFileController;
import de.hepisec.gaecms.csrfguard.CMSCsrfGuard;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.csrfguard.CsrfGuardException;

/**
 *
 * @author Hendrik Pilz
 */
public class UploadMediaFile extends HttpServlet {

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
        response.sendRedirect("/admin/MediaLibrary");
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
        MediaFileController controller = new MediaFileController(getServletContext());
        controller.handleUpload(request, response);
        response.sendRedirect("/admin/MediaLibrary");
    }
}

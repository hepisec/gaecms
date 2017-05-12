package de.hepisec.gaecms.servlet;

import de.hepisec.gaecms.controller.BlogEntryController;
import de.hepisec.gaecms.csrfguard.CMSCsrfGuard;
import de.hepisec.gaecms.model.BlogEntry;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.csrfguard.CsrfGuardException;

/**
 *
 * @author Hendrik Pilz
 */
public class Write extends HttpServlet {

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
        BlogEntry blogEntry = new BlogEntry();
        String id = request.getParameter("id");
        
        if (id != null) {        
            BlogEntryController controller = new BlogEntryController();
            blogEntry = controller.findById(id);
        }
        
        request.setAttribute("entry", blogEntry);
        request.getRequestDispatcher("/admin/write.jsp").forward(request, response);
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
        try {
            CMSCsrfGuard.validateRequest(request);
            BlogEntryController controller = new BlogEntryController();
            controller.handleRequest(request, response);
            response.sendRedirect("/admin/");
        } catch (CsrfGuardException ex) {
            throw new IOException(ex);
        }
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

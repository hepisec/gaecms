package de.hepisec.gaecms;

import de.hepisec.gaecms.controller.BaseController;
import static de.hepisec.gaecms.TypeUtil.parseInt;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Hendrik Pilz
 */
public class Pager<T> {
    private BaseController<T> controller;
    private int limit;
    
    public Pager(BaseController<T> controller, int limit) {
        this.controller = controller;
        this.limit = limit;
    }
    
    public void apply(HttpServletRequest request) {
        int page = parseInt(request.getParameter("page"), 0);                           
        int offset = page * limit;

        List<T> entries = controller.findAll(offset, limit);
        int numberOfEntries = controller.count();
        
        request.setAttribute("entries", entries);
        request.setAttribute("numberOfEntries", numberOfEntries);
        request.setAttribute("entriesPerPage", limit);
        request.setAttribute("offset", offset);
        request.setAttribute("page", page);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("previousPage", page - 1);        
    }
}

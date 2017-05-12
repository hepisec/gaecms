package de.hepisec.gaecms.controller;

import com.google.appengine.api.datastore.Query;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import de.hepisec.gaecms.model.BlogEntry;
import de.hepisec.validation.ValidationException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hendrik Pilz
 */
public class BlogEntryController extends BaseController<BlogEntry> {

    private boolean isAdmin = false;

    public BlogEntryController() {
        this(false);
    }

    public BlogEntryController(boolean isAdmin) {
        super(BlogEntry.class);
        this.isAdmin = isAdmin;
    }

    public String handleRequest(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String id = request.getParameter("id");
        final String publish = request.getParameter("publish");

        try {
            String entityId = ofy().transact(new Work<String>() {
                @Override
                public String run() {
                    Date datePublished = null;

                    if (publish != null) {
                        datePublished = new Date();
                    }

                    BlogEntry blogEntry = findById(id);

                    if (blogEntry == null) {
                        try {
                            return create(id, request.getParameter("title"), request.getParameter("text"), datePublished);
                        } catch (ValidationException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        if (request.getParameter("edit") == null && publish == null) {
                            // trying to create an entry with a duplicate ID
                            // if we don't throw the exception here, the existing entry will get overwritten
                            throw new RuntimeException(new IOException("ID already exists"));
                        }
                    }

                    try {
                        return edit(blogEntry, request.getParameter("title"), request.getParameter("text"), datePublished);
                    } catch (ValidationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            
            return entityId;
        } catch (Exception ex) {
            Logger.getLogger(BlogEntryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IOException(ex);
        }
    }

    private String create(String id, String title, String text, Date datePublished) throws ValidationException {
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setId(id);
        blogEntry.setDateCreated(new Date());

        return edit(blogEntry, title, text, datePublished);
    }

    private String edit(BlogEntry blogEntry, String title, String text, Date datePublished) throws ValidationException {
        blogEntry.setTitle(title);
        blogEntry.setText(text);
        blogEntry.setDatePublished(datePublished);

        return save(blogEntry).getName();
    }

    @Override
    public List<BlogEntry> findAll(int offset, int limit) {
        LoadType<BlogEntry> load = ofy().load().type(BlogEntry.class);
        List<BlogEntry> blogEntries;

        if (isAdmin) {
            blogEntries = load.order("-datePublished").limit(limit).offset(offset).list();
        } else {
            blogEntries = load.filter(new Query.FilterPredicate("datePublished", Query.FilterOperator.NOT_EQUAL, null)).order("-datePublished").limit(limit).offset(offset).list();
        }

        return blogEntries;
    }

    @Override
    public int count() {
        if (isAdmin) {
            return super.count();
        } else {
            return ofy().load().type(BlogEntry.class).filter(new Query.FilterPredicate("datePublished", Query.FilterOperator.NOT_EQUAL, null)).count();
        }
    }
}

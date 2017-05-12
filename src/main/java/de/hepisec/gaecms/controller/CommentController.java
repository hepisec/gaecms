package de.hepisec.gaecms.controller;

import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import static de.hepisec.gaecms.TypeUtil.parseLong;
import de.hepisec.gaecms.model.BlogEntry;
import de.hepisec.gaecms.model.Comment;
import de.hepisec.validation.ValidationException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hendrik Pilz
 */
public class CommentController extends BaseController<Comment> {

    public CommentController() {
        super(Comment.class);
    }

    public long handleRequest(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        final long id = parseLong(request.getParameter("id"), 0);
        final String blogId = request.getParameter("blog_id");

        try {
            Long entityId = ofy().transact(new Work<Long>() {
                @Override
                public Long run() {
                    BlogEntryController blogEntryController = new BlogEntryController();
                    BlogEntry blogEntry = blogEntryController.findById(blogId);

                    if (blogEntry == null) {
                        throw new RuntimeException(new IOException("Trying to save comment for unknown blog entry."));
                    }

                    if (id == 0) {
                        try {
                            return create(blogEntry, request.getRemoteAddr(), request.getParameter("name"), request.getParameter("text"));
                        } catch (ValidationException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Comment comment = findByIdWithParent(BlogEntry.class, blogEntry.getId(), id);

                        try {
                            return edit(comment, blogEntry, request.getRemoteAddr(), request.getParameter("name"), request.getParameter("text")).getId();
                        } catch (ValidationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });

            return entityId;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    private long create(BlogEntry blogEntry, String ipAddress, String name, String text) throws ValidationException {
        Key<Comment> commentKey = edit(new Comment(), blogEntry, ipAddress, name, text);
        blogEntry.getCommentsRef().add(Ref.create(commentKey));
        ofy().save().entity(blogEntry).now();
        return commentKey.getId();
    }

    private Key<Comment> edit(Comment comment, BlogEntry blogEntry, String ipAddress, String name, String text) throws ValidationException {
        comment.setBlogEntry(blogEntry);
        comment.setIpAddress(ipAddress);
        comment.setName(name);
        comment.setText(text);

        return save(comment);
    }
}

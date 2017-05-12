package de.hepisec.gaeblog;

import de.hepisec.gaecms.controller.BlogEntryController;
import de.hepisec.gaecms.controller.CommentController;
import static org.junit.Assert.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import de.hepisec.gaecms.model.BlogEntry;
import de.hepisec.gaecms.model.Comment;
import de.hepisec.validation.ValidationException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Hendrik Pilz
 */
public class CommentControllerTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    protected Closeable session;
    
    public CommentControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        session = ObjectifyService.begin();
        ObjectifyService.register(BlogEntry.class);
        ObjectifyService.register(Comment.class);
        helper.setUp();
    }
    
    @After
    public void tearDown() {
        session.close();
        helper.tearDown();
    }

    @Test(expected = IOException.class)
    public void testEmptyRequest() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        
        CommentController controller = new CommentController();              
        controller.handleRequest(request, response);
    }
        
    @Test
    public void testRequestAddAndEditComment() throws ValidationException {
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setId("test");
        blogEntry.setDateCreated(new Date());
        blogEntry.setTitle("title");
        blogEntry.setText("text");
        BlogEntryController blogController = new BlogEntryController();
        String blogId = blogController.save(blogEntry).getName();
        
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("blog_id")).thenReturn(blogId + "");
        Mockito.when(request.getParameter("name")).thenReturn("name");
        Mockito.when(request.getParameter("text")).thenReturn("text");
        Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        CommentController controller = new CommentController();        
        assertEquals(0, controller.count());

        long commentId = 0;
        
        try {        
            commentId = controller.handleRequest(request, response);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        assertEquals(1, controller.count());
        
        blogEntry = blogController.findById(blogId);
        assertEquals(1, blogEntry.getCommentsRef().size());
        assertEquals(1, blogEntry.getComments().size());
        
        Comment createdComment = controller.findByIdWithParent(BlogEntry.class, blogEntry.getId(), commentId);
        
        assertNotEquals(null, createdComment);
        
        // edit the comment
        Mockito.when(request.getParameter("id")).thenReturn(commentId + "");
        Mockito.when(request.getParameter("text")).thenReturn("text edited");        
        
        try {        
            controller.handleRequest(request, response);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        assertEquals(1, controller.count());
        
        blogEntry = blogController.findById(blogId);
        assertEquals(1, blogEntry.getCommentsRef().size());
        assertEquals(1, blogEntry.getComments().size());        
    }    
}

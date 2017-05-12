package de.hepisec.gaeblog;

import de.hepisec.gaecms.controller.BlogEntryController;
import static org.junit.Assert.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import de.hepisec.gaecms.model.BlogEntry;
import java.io.IOException;
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
public class BlogEntryControllerTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    protected Closeable session;
    
    public BlogEntryControllerTest() {
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
        
        BlogEntryController controller = new BlogEntryController();        
        controller.handleRequest(request, response);
    }
        
    @Test
    public void testRequestAddBlogEntry() {        
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("id")).thenReturn("id1");
        Mockito.when(request.getParameter("title")).thenReturn("title");
        Mockito.when(request.getParameter("text")).thenReturn("text");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        BlogEntryController controller = new BlogEntryController();        
        assertEquals(0, controller.count());

        try {        
            controller.handleRequest(request, response);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        BlogEntryController adminController = new BlogEntryController(true);
        assertEquals(0, controller.count());
        assertEquals(1, adminController.count());
        assertEquals(0, controller.findAll(0, 1).size());
        assertEquals(1, adminController.findAll(0, 1).size());
    }    
    
    @Test(expected = IOException.class)
    public void testRequestAddDuplicateBlogEntry() throws IOException {       
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);        
        Mockito.when(request.getParameter("id")).thenReturn("id2");
        Mockito.when(request.getParameter("title")).thenReturn("title");
        Mockito.when(request.getParameter("text")).thenReturn("text");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        BlogEntryController controller = new BlogEntryController();        
        assertEquals(0, controller.count());

        try {        
            controller.handleRequest(request, response);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        BlogEntryController adminController = new BlogEntryController(true);
        assertEquals(0, controller.count());
        assertEquals(1, adminController.count());
        assertEquals(0, controller.findAll(0, 1).size());
        assertEquals(1, adminController.findAll(0, 1).size());
        
        controller.handleRequest(request, response);
    }      
    
    @Test
    public void testRequestPublishBlogEntry() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("id")).thenReturn("id3");
        Mockito.when(request.getParameter("publish")).thenReturn("publish");
        Mockito.when(request.getParameter("title")).thenReturn("title");
        Mockito.when(request.getParameter("text")).thenReturn("text");
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        BlogEntryController controller = new BlogEntryController();        
        assertEquals(0, controller.count());

        try {        
            controller.handleRequest(request, response);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        BlogEntryController adminController = new BlogEntryController(true);
        assertEquals(1, controller.count());
        assertEquals(1, adminController.count());
        assertEquals(1, controller.findAll(0, 1).size());
        assertEquals(1, adminController.findAll(0, 1).size());
    }        
}

package de.hepisec.gaeblog;

import de.hepisec.gaecms.model.BlogEntry;
import de.hepisec.validation.Validation;
import de.hepisec.validation.ValidationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hendrik Pilz
 */
public class BlogEntryValidationTest {
    
    public BlogEntryValidationTest() {
    }

    @Test
    public void testBlogEntryValidation() {
        Validation validation = new Validation();
        BlogEntry blogEntry = new BlogEntry();
        
        // Test empty Title, PermaLink and Text
        try {
            validation.validate(blogEntry);
            fail();
        } catch (ValidationException ex) {
            
        }
        
        // Test empty Text
        try {
            blogEntry.setTitle("Title");
            validation.validate(blogEntry);
            fail();
        } catch (ValidationException ex) {
            
        }
                
        // Test empty Title
        try {
            blogEntry.setTitle(null);
            blogEntry.setText("Text");
            validation.validate(blogEntry);
            fail();
        } catch (ValidationException ex) {
            
        }
        
        // Test with Title and Text
        try {
            blogEntry.setTitle("Title");
            validation.validate(blogEntry);
        } catch (ValidationException ex) {
            fail();
        }
    }
}

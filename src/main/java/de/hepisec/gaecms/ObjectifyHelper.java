package de.hepisec.gaecms;

import com.googlecode.objectify.ObjectifyService;
import de.hepisec.gaecms.model.BlogEntry;
import de.hepisec.gaecms.model.Comment;
import de.hepisec.gaecms.model.MediaFile;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Hendrik Pilz
 */
public class ObjectifyHelper implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ObjectifyService.register(BlogEntry.class);
        ObjectifyService.register(Comment.class);
        ObjectifyService.register(MediaFile.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing
    }

}

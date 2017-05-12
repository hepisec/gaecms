package de.hepisec.gaecms.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import de.hepisec.validation.annotations.NotNullOrEmpty;
import java.util.Date;

/**
 *
 * @author Hendrik Pilz
 */
@Entity
public class Comment {
    @Id
    private Long id;
    @Parent
    private Ref<BlogEntry> blogEntry;
    @Index
    private Date dateCreated = new Date();
    private String name;
    @NotNullOrEmpty
    private String text;
    private String ipAddress;

    public Comment() {
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlogEntry getBlogEntry() {
        return blogEntry.get();
    }

    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = Ref.create(blogEntry);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        if (this.dateCreated == null) {
            this.dateCreated = dateCreated;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

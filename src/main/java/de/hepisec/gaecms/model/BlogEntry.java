package de.hepisec.gaecms.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import de.hepisec.validation.annotations.NotNullOrEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Hendrik Pilz
 */
@Entity
public class BlogEntry {

    @Id
    private String id;
    @Index
    private Date dateCreated = new Date();
    @Index
    private Date datePublished = null;
    @NotNullOrEmpty
    private String title;
    @NotNullOrEmpty
    private String text;
    @Load
    private List<Ref<Comment>> comments;

    public BlogEntry() {
        comments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        if (this.dateCreated == null) {
            this.dateCreated = dateCreated;
        }
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        if (this.datePublished == null) {
            this.datePublished = datePublished;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Ref<Comment>> getCommentsRef() {
        return comments;
    }

    public List<Comment> getComments() {
        List<Comment> commentObjects = new ArrayList<>();

        for (Ref<Comment> ref : comments) {
            commentObjects.add(ref.get());
        }

        return commentObjects;
    }

    public void setComments(List<Ref<Comment>> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "BlogEntry{" + "id=" + id + ", dateCreated=" + dateCreated + ", datePublished=" + datePublished + ", title=" + title + ", text=" + text + ", comments=" + comments + '}';
    }
}

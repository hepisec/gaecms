package de.hepisec.gaecms.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import de.hepisec.validation.annotations.NotNullOrEmpty;
import java.util.Date;

/**
 *
 * @author hendrik
 */
@Entity
public class MediaFile {
    @Id
    private Long id;
    @Index
    private Date dateCreated = new Date();
    private String description = "";
    @NotNullOrEmpty
    private String blobId;

    public MediaFile() {        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlobId() {
        return blobId;
    }

    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }
}

package de.hepisec.gaecms.controller;

import com.google.appengine.api.utils.SystemProperty;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import static com.googlecode.objectify.ObjectifyService.ofy;
import de.hepisec.gaecms.csrfguard.CMSCsrfGuard;
import de.hepisec.gaecms.model.MediaFile;
import de.hepisec.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.owasp.csrfguard.CsrfGuard;
import org.owasp.csrfguard.CsrfGuardException;

/**
 *
 * @author Hendrik Pilz
 */
public class MediaFileController extends BaseController<MediaFile> {

    private ServletContext servletContext;
    private List<Acl> defaultAcl;

    public MediaFileController(ServletContext servletContext) {
        super(MediaFile.class);
        this.servletContext = servletContext;
        
        defaultAcl = new ArrayList<>();
        defaultAcl.add(Acl.newBuilder(Acl.User.ofAllUsers(), Acl.Role.READER).build());
    }

    /**
     *
     * @param request
     * @param response
     * @return name of the uploaded file, may return null if the request is no
     * multipart request
     * @throws IOException
     */
    public String handleUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return null;
        }

        Storage storage = StorageOptions.getDefaultInstance().getService();
        ServletFileUpload upload = new ServletFileUpload();
        String uploadedFileName = null;
        boolean csrfCheck = false;
        
        try {
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getName();

                try (InputStream is = item.openStream()) {
                    if (item.isFormField() && item.getFieldName().equals(CsrfGuard.getInstance().getTokenName())) {
                        try {
                            String tokenFromRequest = IOUtils.toString(is, "UTF-8");
                            CMSCsrfGuard.validateRequest(request, tokenFromRequest);
                            csrfCheck = true;
                        } catch (CsrfGuardException ex) {
                            throw new IOException(ex);
                        }
                    }
                    
                    if (!item.isFormField() && csrfCheck) { // is uploaded file
                        BlobId blobId = BlobId.of(getBucket(), "media/" + name);

                        // if file exists, append counter
                        int c = 0;

                        while (null != storage.get(blobId)) {
                            blobId = BlobId.of(getBucket(), "media/" + name + "_" + c++);
                        }

                        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(item.getContentType()).setAcl(defaultAcl).build();

                        try (WriteChannel writer = storage.writer(blobInfo)) {
                            byte[] buffer = new byte[4096];
                            int length;

                            while ((length = is.read(buffer)) != -1) {
                                writer.write(ByteBuffer.wrap(buffer, 0, length));
                            }
                        }

                        MediaFile mediaFile = new MediaFile();
                        mediaFile.setDateCreated(new Date());
                        mediaFile.setBlobId(blobId.getName());
                        uploadedFileName = save(mediaFile).getName();
                    }
                }
            }
        } catch (FileUploadException ex) {
            throw new IOException(ex);
        }

        return uploadedFileName;
    }

    private String edit(MediaFile mediaFile, String description) throws ValidationException {
        mediaFile.setDescription(description);

        return save(mediaFile).getName();
    }

    @Override
    public List<MediaFile> findAll(int offset, int limit) {
        return ofy().load().type(MediaFile.class).order("-dateCreated").limit(limit).offset(offset).list();
    }

    private String getBucket() {
        String bucket = servletContext.getInitParameter("bucket");

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
            bucket = servletContext.getInitParameter("staging-bucket");
        }

        if (null == bucket) {
            bucket = "";
        }

        return bucket;
    }
}

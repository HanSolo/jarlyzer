package eu.hansolo;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.multipart.StreamingFileUpload;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;


@Controller("/jarlyzer")
//@Secured(SecurityRule.IS_ANONYMOUS)
public class ControllerV1 {

    // ******************** Upload JAR file ***********************************
    @Post
    @Value("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> upload(final CompletedFileUpload file) {
        try {
            if (file.getFilename().endsWith(".jar")) {
                try(FileOutputStream fos = new FileOutputStream(file.getFilename())) {
                    fos.write(file.getBytes());
                    File           jarfile  = new File(file.getFilename());
                    TreeNode<Item> treeNode = Scanner.getClassesAndMethods(jarfile.getName());
                    String         jsonText = Helper.toJsonString(jarfile.getName(), treeNode, "", "", "");
                    //jarfile.delete();

                    HttpResponse response = HttpResponse.ok(jsonText).contentType(MediaType.APPLICATION_JSON_TYPE).status(HttpStatus.OK);
                    return response;
                } catch (Exception e) {
                    HttpResponse response = HttpResponse.badRequest("{}")
                                                        .contentType(MediaType.APPLICATION_JSON_TYPE)
                                                        .status(HttpStatus.BAD_REQUEST);
                    return response;
                }
            } else {
                HttpResponse response = HttpResponse.badRequest("{}")
                                                    .contentType(MediaType.APPLICATION_JSON_TYPE)
                                                    .status(HttpStatus.BAD_REQUEST);
                return response;
            }
        } catch (Exception e) {
            HttpResponse response = HttpResponse.badRequest("{}")
                                                .contentType(MediaType.APPLICATION_JSON_TYPE)
                                                .status(HttpStatus.BAD_REQUEST);
            return response;
        }
    }
}

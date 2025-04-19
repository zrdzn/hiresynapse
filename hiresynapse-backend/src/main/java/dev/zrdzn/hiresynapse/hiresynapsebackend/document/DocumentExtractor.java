package dev.zrdzn.hiresynapse.hiresynapsebackend.document;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentExtractor {

    boolean supports(String fileName);

    String extractText(InputStream fileStream) throws IOException;

}

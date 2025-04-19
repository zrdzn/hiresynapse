package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.document.DocumentExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DocumentExtractorService {

    private final List<DocumentExtractor> extractors;

    public DocumentExtractorService(List<DocumentExtractor> extractors) {
        this.extractors = extractors;
    }

    public String extractText(String fileName, InputStream fileStream) throws IOException {
        return extractors.stream()
            .filter(extractor -> extractor.supports(fileName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + fileName))
            .extractText(fileStream);
    }

}

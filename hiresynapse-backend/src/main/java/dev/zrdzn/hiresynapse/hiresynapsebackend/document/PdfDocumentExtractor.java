package dev.zrdzn.hiresynapse.hiresynapsebackend.document;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String fileName) {
        return fileName.endsWith(".pdf");
    }

    @Override
    public String extractText(InputStream fileStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(fileStream.readAllBytes())) {
            return new PDFTextStripper().getText(document);
        }
    }

}

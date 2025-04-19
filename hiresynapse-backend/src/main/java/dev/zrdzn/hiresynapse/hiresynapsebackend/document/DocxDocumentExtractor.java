package dev.zrdzn.hiresynapse.hiresynapsebackend.document;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Component
public class DocxDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String fileName) {
        return fileName.endsWith(".docx") || fileName.endsWith(".doc");
    }

    @Override
    public String extractText(InputStream fileStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(fileStream)) {
            return document.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining("\n"));
        }
    }

}

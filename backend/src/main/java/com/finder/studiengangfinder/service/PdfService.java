package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.ResultDto;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    public byte[] generateResultPdf(ResultDto result) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Studiengangs-Finder Ergebnis", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("Nickname: " + result.nickname()));
            document.add(new Paragraph("Datum: " + result.generatedAt().toLocalDate()));
            document.add(new Paragraph("Persoenlicher Link: " + frontendBaseUrl + "/results/" + result.token()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Profilzusammenfassung", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(new Paragraph(result.profileSummary()));
            document.add(new Paragraph("Interessen: " + String.join(", ", result.selectedInterests())));
            document.add(new Paragraph("Skills: " + String.join(", ", result.selectedSkills())));
            document.add(new Paragraph("Praeferenzen: " + String.join(", ", result.selectedPreferences())));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            addCell(table, "Studiengang", true);
            addCell(table, "Score", true);
            addCell(table, "Karriereperspektiven", true);
            result.recommendations().stream().limit(4).forEach(rec -> {
                addCell(table, rec.programName(), false);
                addCell(table, rec.score() + "%", false);
                addCell(table, String.join(", ", rec.details().careerPaths().stream().map(c -> c.title()).limit(3).toList()), false);
            });
            document.add(table);

            if (result.topRecommendation() != null) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Top-Empfehlung: " + result.topRecommendation().programName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph("Risikoanalyse: " + result.topRecommendation().dynamicRiskSummary()));
                document.add(new Paragraph("Bewerbungs-Checkliste:"));
                result.topRecommendation().details().applicationChecklist().forEach(item -> {
                    try {
                        document.add(new Paragraph("- " + item));
                    } catch (Exception ignored) {
                    }
                });
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("PDF konnte nicht erzeugt werden", exception);
        }
    }

    private void addCell(PdfPTable table, String text, boolean header) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(header ? FontFactory.HELVETICA_BOLD : FontFactory.HELVETICA, 11)));
        cell.setPadding(8f);
        table.addCell(cell);
    }
}

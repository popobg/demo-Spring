package fr.diginamic.hello.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.hello.models.Ville;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    public static void generateDocumentPdfVilles(HttpServletResponse response, List<Ville> villes) throws IOException, DocumentException {
        Document doc = new Document(PageSize.A4);
        // Ecrit les données binaires dans la réponse HTTP
        PdfWriter.getInstance(doc, response.getOutputStream());

        doc.open();
        doc.addTitle(String.format("fiche_villes_%s", villes.getFirst().getDepartement().getNom()));
        doc.newPage();
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

        Phrase title = new Phrase(String.format("Liste des villes du département %s (France) :\n\n", villes.getFirst().getDepartement().getNom()), new Font(baseFont, 16, 1, new BaseColor(235, 64, 52)));
        doc.add(title);

        for (Ville ville: villes) {
            Phrase p = new Phrase(ville.toString(), new Font(baseFont, 12, 1, new BaseColor(0, 51, 80)));
            doc.add(p);
        }
        
        doc.close();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.iTextDemo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultPlugin;
import org.joget.plugin.base.PluginProperty;
import org.joget.plugin.base.PluginWebSupport;

/**
 *
 * @author mabub
 */
public class TestPDF extends DefaultPlugin implements PluginWebSupport
{

    public String getName()
    {
        return "Test PDF Plugin";
    }

    public String getVersion()
    {
        return "1.0.0";
    }

    public String getDescription()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PluginProperty[] getPluginProperties()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object execute(Map map)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String refNo = request.getParameter("refNo");
        String query = "SELECT * FROM app_fd_ta_testingapp Where c_refNo = ?";
        TreeMap<String, String> pdfData = new TreeMap<String, String>();
        int i = 0;

        try
        {
            DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, refNo);
            ResultSet rSet = stmt.executeQuery();

            while (rSet.next())
            {
                pdfData.put("Ref No", rSet.getString("c_refNo"));

                pdfData.put("Accounting System No", rSet.getString("c_accNo"));
                pdfData.put("Create By", rSet.getString("c_createBy"));
                pdfData.put("Requesting BU", rSet.getString("c_requestingBU"));
                pdfData.put("Description", rSet.getString("c_description"));
                pdfData.put("Amount", rSet.getString("c_amount"));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(TestPDF.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            
            int padding = 5;
            BaseColor black = new BaseColor(0,0,0);
            int numOfCols = 2;
            PdfPTable table = new PdfPTable(numOfCols);
            PdfPCell cell;
            FontSelector selector = new FontSelector();
            Font f1 = new Font();
            f1.setColor(BaseColor.WHITE);
            selector.addFont(f1);
            
            Phrase h1 = selector.process("PDF Reporting for eBilling");
            Phrase h2 = selector.process("Field");
            Phrase h3 = selector.process("Description");
            
            cell = new PdfPCell(h1);
            cell.setColspan(2);
            cell.setBackgroundColor(black);
            cell.setPadding(padding);
            table.addCell(cell);
            
            cell = new PdfPCell(h2);
            cell.setBackgroundColor(black);
            //cell.setBorderColor(new BaseColor(200,200,200));
            cell.setPadding(padding);
            table.addCell(cell);
            
            cell = new PdfPCell(h3);
            cell.setBackgroundColor(black); 
            cell.setPadding(padding);
            table.addCell(cell);

            for (Map.Entry<String, String> entry : pdfData.entrySet())
            {
                Phrase key = new Phrase(entry.getKey());
                Phrase value = new Phrase(entry.getValue());
                
                cell = new PdfPCell(key);
                cell.setPadding(padding);
                table.addCell(cell);

                cell = new PdfPCell(value);
                cell.setPadding(padding);
                table.addCell(cell);
            }
            
            document.add(table);
            document.close();

            response.reset();
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (DocumentException ex)
        {

        }

    }

}

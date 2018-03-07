# PDF-Demo-with-JAVA-using-JOGET-workflow-engine

iText is a library for creating and manipulating PDF files in Java.
In this webservice I am doing:

1. Get request parameter for HTTPREQUEST i.e. reference no from calling URL.
2. Get data for corresponding reference no from Database.
3. Save this data in treemap so that we have can have key value structure.
4. Create a Document.
5. Pass document with Byte Output Stream to the PDF writer.
6. Create a table with 2 cells in each row.
7. Add values from database in these cells.
8. Format cells by setting borders, background colors, row spanning and paddings.
9. Close the document and send Byte Output Stream in HTTPSERVLET response.

Output example of generated Pdf file:
![Screenshot](https://github.com/AbuBakrCh/PDF-Demo-with-JAVA-using-JOGET-workflow-engine/blob/master/Images/PDFoutput.PNG)

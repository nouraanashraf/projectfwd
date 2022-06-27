package SIG.Controller;

import SIG.View.Frame;
import SIG.View.HeaderDialog;
import SIG.View.LineDialog;
import SIG.Model.InvoiceHeader;
import SIG.Model.InvoiceHeaderTableModel;
import SIG.Model.InvoiceLine;
import SIG.Model.InvoiceLineTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Nouran Ashraf
 */
public class ActionHandler implements ActionListener{
    private Frame invoiceFrame;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private HeaderDialog newHeaderDialog;
    private LineDialog newLineDialog;

    public Frame getInvoiceFrame() {
        return invoiceFrame;
    }

    public void setInvoiceFrame(Frame invoiceFrame) {
        this.invoiceFrame = invoiceFrame;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public ActionHandler(Frame invoiceFrame) {
        this.invoiceFrame = invoiceFrame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        
        switch(e.getActionCommand()){
        
            case "Load File":
                loadFile();
                break;
            case "Save File":
               saveFile();
                break;
            case "Create New Invoice":
               createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "New Line":
                newLine();
                break;
            case "Delete Line":
                deleteLine();
                break;
            case "newInvoiceOK":
                newInvoiceOK();
                break;
            case "newInvoiceCancel":
                newInvoiceCancel();
                break;
            case "newLineOK":
                newLineOK();
                break;
            case "newLineCancel":
                newLineCancel();
                break;    
        }   
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(invoiceFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] arr = headerLine.split(",");
                    String str1 = arr[0];
                    String str2 = arr[1];
                    String str3 = arr[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = df.parse(str2);
                    InvoiceHeader header = new InvoiceHeader(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                invoiceFrame.setInvoiceList(invoiceHeaders);
                result = fileChooser.showOpenDialog(invoiceFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    for (String lineLine : lineLines) {
                        String[] arr = lineLine.split(",");
                        String str1 = arr[0];
                        String str2 = arr[1];
                        String str3 = arr[2];
                        String str4 = arr[3];
                        int invCode = Integer.parseInt(str1);
                        double price = Double.parseDouble(str3);
                        int count = Integer.parseInt(str4);
                        InvoiceHeader inv = invoiceFrame.getInvObject(invCode);
                        InvoiceLine line = new InvoiceLine(str2, price, count, inv);
                        inv.getLines().add(line);
                    }
                    System.out.println(".................................................................................");
                    System.out.println(".............................READING FILES.......................................");
                    System.out.println(".................................................................................");
                    System.out.println(".............................HEADERS FILE........................................");
                    for (String headerLine : headerLines) {System.out.println(headerLine);}
                    System.out.println(".................................................................................");
                    System.out.println(".............................LINES FILE..........................................");
                    for (String lineLine : lineLines){System.out.println(lineLine);}
                    System.out.println(".................................................................................");
                    System.out.println("..................................DONE...........................................");
                    System.out.println(".................................................................................");
                }
                InvoiceHeaderTableModel invoiceHeaderTableModel = new InvoiceHeaderTableModel(invoiceHeaders);
                invoiceFrame.setInvoiceHeaderTableModel(invoiceHeaderTableModel);
                invoiceFrame.getHTbl().setModel(invoiceHeaderTableModel);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(invoiceFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(invoiceFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        List<InvoiceHeader> invoicesArray= invoiceFrame.getInvoiceList();
        JFileChooser fc = new JFileChooser();
            try{

                int result = fc.showSaveDialog(invoiceFrame);
                if(result==JFileChooser.APPROVE_OPTION){
                    File headerFile = fc.getSelectedFile();
                    FileWriter filewriter = new FileWriter(headerFile);
                    String headers="";    
                    String lines="";
                    for(InvoiceHeader invoice : invoicesArray){

                        headers+=invoice.toString();
                        headers+="\n";
                        for(InvoiceLine line : invoice.getLines()){
                            lines+=line.toString();
                            lines+="\n";
                        }
                    }
                    headers = headers.substring(0, headers.length()-1);
                    lines = lines.substring(0, lines.length()-1);
                    
                    result = fc.showSaveDialog(invoiceFrame);
                    File lineFile = fc.getSelectedFile();
                    FileWriter fileW = new FileWriter(lineFile);
                    filewriter.write(headers);
                    fileW.write(lines);
                    filewriter.close();
                    fileW.close();
                }
            }catch(IOException ex){
             JOptionPane.showMessageDialog(invoiceFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }



    }

    private void createNewInvoice() {
        newHeaderDialog = new HeaderDialog(invoiceFrame);
        newHeaderDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvIndex = invoiceFrame.getHTbl().getSelectedRow();
        if(selectedInvIndex !=-1){
        invoiceFrame.getInvoiceList().remove(selectedInvIndex);
        invoiceFrame.getInvoiceHeaderTableModel().fireTableDataChanged();
        
        invoiceFrame.getLTbl().setModel(new InvoiceLineTableModel(null));
        invoiceFrame.setLinesList(null);
            invoiceFrame.getCustName().setText("");
            invoiceFrame.getInvNumber().setText("");
            invoiceFrame.getInvTotal().setText("");
            invoiceFrame.getInvDate().setText(""); 
        
        }
    }

    private void newLine() {
        newLineDialog = new LineDialog(invoiceFrame);
        newLineDialog.setVisible(true);
    }

    private void deleteLine() {
        int selectedLineIndex = invoiceFrame.getLTbl().getSelectedRow();
        int selectedInvoiceIndex = invoiceFrame.getHTbl().getSelectedRow();
        if(selectedLineIndex != -1){
        invoiceFrame.getLinesList().remove(selectedLineIndex);
        InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) invoiceFrame.getLTbl().getModel();
        lineTableModel.fireTableDataChanged();
        invoiceFrame.getInvTotal().setText(""+invoiceFrame.getInvoiceList().get(selectedInvoiceIndex).getInvTotal());
        invoiceFrame.getInvoiceHeaderTableModel().fireTableDataChanged();
        invoiceFrame.getHTbl().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
                
        
        }
        
    }

    private void newInvoiceOK() {
         newHeaderDialog.setVisible(false);
         
         String custName = newHeaderDialog.getCustNameField().getText();
         String str = newHeaderDialog.getInvDateField().getText();
         Date d = new Date();
         try{
             d = df.parse(str);
         }catch(ParseException ex){
         
         JOptionPane.showMessageDialog(invoiceFrame, "Cannot parse date, reseting to today","Invalid Date format",JOptionPane.ERROR_MESSAGE);
         }
         int invNum= 0;
         for(InvoiceHeader inv : invoiceFrame.getInvoiceList()){
             if(inv.getInvNum() > invNum ) invNum = inv.getInvNum();
         }
         invNum++;
         InvoiceHeader newinv = new InvoiceHeader(invNum, custName, d);
        invoiceFrame.getInvoiceList().add(newinv);
        invoiceFrame.getInvoiceHeaderTableModel().fireTableDataChanged();
        newHeaderDialog.dispose();
        newHeaderDialog = null;
    }

    private void newInvoiceCancel() {
        newHeaderDialog.setVisible(false);
        newHeaderDialog.dispose();
        newHeaderDialog = null;
    }

    private void newLineOK() {
        newLineDialog.setVisible(false);
        
        String name = newLineDialog.getItemNameField().getText();
        String str1 = newLineDialog.getItemCountField().getText();
        String str2 = newLineDialog.getItemPriceField().getText();
        int  count = 1;
        double price = 1;
        try{
             count = Integer.parseInt(str1);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(invoiceFrame, "Cannot convert number","Invalid number format ",JOptionPane.ERROR_MESSAGE);
        }
         try{
             price = Double.parseDouble(str2);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(invoiceFrame, "Cannot convert number","Invalid number format ",JOptionPane.ERROR_MESSAGE);
        }
         
         int selectedInvHeader = invoiceFrame.getHTbl().getSelectedRow();
         if( selectedInvHeader != -1){
         InvoiceHeader invHeader = invoiceFrame.getInvoiceList().get(selectedInvHeader);
         InvoiceLine line = new InvoiceLine(name, price, count, invHeader);
         invoiceFrame.getLinesList().add(line);
         InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) invoiceFrame.getLTbl().getModel();
         lineTableModel.fireTableDataChanged();
         invoiceFrame.getInvoiceHeaderTableModel().fireTableDataChanged();
         invoiceFrame.getHTbl().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
         }
        
        newLineDialog.dispose();
        newLineDialog = null;
    }

    private void newLineCancel() {
        newLineDialog.setVisible(false);
    }

}
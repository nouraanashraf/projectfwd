package SIG.Controller;
import SIG.View.Frame;
import SIG.Model.InvoiceHeader;
import SIG.Model.InvoiceLine;
import SIG.Model.InvoiceLineTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Nouran Ashraf
 */
public class SelectionListener implements ListSelectionListener {

    private Frame invoiceFrame;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    public SelectionListener(Frame frame) {
        this.invoiceFrame = frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvIndex = invoiceFrame.getHTbl().getSelectedRow();
        if (selectedInvIndex != -1) {
            InvoiceHeader selectedInv = invoiceFrame.getInvoiceList().get(selectedInvIndex);
            ArrayList<InvoiceLine> lines = selectedInv.getLines();
            InvoiceLineTableModel lineTableModel = new InvoiceLineTableModel(lines);
            invoiceFrame.setLinesList(lines);
            invoiceFrame.getLTbl().setModel(lineTableModel);
            invoiceFrame.getCustName().setText(selectedInv.getCustomerName());
            invoiceFrame.getInvNumber().setText("" + selectedInv.getInvNum());
            invoiceFrame.getInvTotal().setText("" + selectedInv.getInvTotal());
            invoiceFrame.getInvDate().setText(df.format(selectedInv.getInvDate()));
        }
    }

}

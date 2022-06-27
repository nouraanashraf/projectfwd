package SIG.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Nouran Ashraf
 */
 public class InvoiceHeader {
    private int invNum ;
    private String customerName;
    private Date invDate;
    private ArrayList <InvoiceLine> lines;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public InvoiceHeader() {
    }

    public InvoiceHeader(int invNum, String customerName, Date invDate) {
        this.invNum = invNum;
        this.customerName = customerName;
        this.invDate = invDate;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public int getInvNum() {
        return invNum;
    }

    public void setInvNum(int invNum) {
        this.invNum = invNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
  public ArrayList <InvoiceLine> getLines() {
      if(lines==null){
      lines= new ArrayList<>();
      }
        return lines;
    }

    public void setLines(ArrayList <InvoiceLine> lines) {
        this.lines = lines;
    }
    
    @Override
    public String toString() {
        return  invNum + "," +  df.format(invDate) +"," + customerName ;
    }

    public double getInvTotal() {
        double total = 0.0;
        for(InvoiceLine line : getLines()){
        total += line.getLineTotal();
        }
        return total;
    }
    public void addInvLine(InvoiceLine line){
    getLines().add(line);
    }

  
  
    
}

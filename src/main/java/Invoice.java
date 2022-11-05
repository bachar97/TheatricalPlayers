import java.text.NumberFormat;
import java.util.*;

public class Invoice {

  public Customer customer;
  public List<Performance> performances;
  private float totalPrice;
  private int volumeCredits;
  private float fidelityDiscount;

  final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

  public Invoice(Customer customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
  }

  private void calculateInvoice(HashMap<String, Play> plays) {
    this.volumeCredits = 0;
    this.totalPrice = 0;
    this.fidelityDiscount = 0;

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      totalPrice += play.calculatePrice(perf.audience);
      volumeCredits += play.calculateCredits(perf.audience);
    }

    this.customer.fidelityBalance += volumeCredits;

    if (this.customer.fidelityBalance >= 150) {
      fidelityDiscount = 15;
      totalPrice -= fidelityDiscount;
      this.customer.fidelityBalance -= 150;
    }
  }

  public String printText(HashMap<String, Play> plays) {

    calculateInvoice(plays);

    StringBuffer result = new StringBuffer(String.format("Invoice for %s:\n", this.customer.customerName));

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result.append(String.format("  %s: %s (%s seats)\n", play.name, frmt.format(play.calculatePrice(perf.audience)), perf.audience));
    }

    result.append(String.format("Total Amount: %s\n", frmt.format(this.totalPrice)));
    result.append(String.format("You earned %s Fidelity Points.\n", volumeCredits));

    if (this.fidelityDiscount > 0) {
      result.append(String.format("You earned a discount of %s due to your Fidelity Points!\n", frmt.format(this.fidelityDiscount)));
    }

    result.append(String.format("Your new Fidelity Points Balance is %s.", this.customer.fidelityBalance));
    return result.toString();

  }

  public String printHTML(HashMap<String, Play> plays) {

    calculateInvoice(plays);

    StringBuffer result = new StringBuffer("<!DOCTYPE html>\n");
    result.append("<html>\n");
    result.append("<head>\n");
    result.append("<style>\n");
    result.append("table, th, td {border: 1px solid black ; width: 500px ; text-align: center ; }\n");
    result.append("caption{padding-top: 10px; caption-side: bottom;}\n");
    result.append("</style>\n");
    result.append("</head>\n");
    result.append("<body>\n");
    result.append("""
            <h1>Invoice</h1>
            <p><b>Client: </b>BigCo</p>
            <table>
            <tr>
            <th>Piece</th>
            <th>Seats Sold</th>
            <th>Price</th>
            </tr>
            """);

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result.append(String.format("""
                      <tr>
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                      </tr>
                      """
              , play.name, perf.audience, frmt.format(play.calculatePrice(perf.audience))));
    }

    result.append(String.format("""
                    <tr>
                    <th colspan="2">Total Owed:</th>
                    <td>%s</td>
                    </tr>
                    """
            , frmt.format(this.totalPrice)));

    result.append(String.format("""
            <tr>
            <th colspan="2">Fidelity Points Earned:</th>
            <td>51</td>
            </tr>
            """, volumeCredits));

    if (this.fidelityDiscount > 0) {
      result.append(String.format("""
              <tr>
              <th colspan="2">Fidelity Points Discount:</th>
              <td>%s</td>
              </tr>
              """, frmt.format(this.fidelityDiscount)));
    }
    result.append(String.format("""
            <tr>
            <th colspan="2">New Fidelity Points:</th>
            <td>%s</td>
            </tr>
            """, this.customer.fidelityBalance));

    result.append("<caption><i>Payment is required in under 30 days. Daily newsletters that you can't unsubscribe from will be e-mailed to you if you don't.</i></caption>\n");
    result.append("</table>\n");
    result.append("</body>\n");
    result.append("</html>\n");

    return result.toString();

  }
}
import java.text.NumberFormat;
import java.util.*;

public class Invoice {

  public Customer customer;
  public List<Performance> performances;
  private float totalPrice;
  private int earnedCredits;
  private float fidelityDiscount;

  final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

  public Invoice(Customer customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
  }

  private void calculateInvoice(HashMap<String, Play> plays) {
    this.earnedCredits = 0;
    this.totalPrice = 0;
    this.fidelityDiscount = 0;

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      totalPrice += play.calculatePrice(perf.audience);
      earnedCredits += play.calculateCredits(perf.audience);
    }

    this.customer.fidelityBalance += earnedCredits;

    if (this.customer.fidelityBalance >= 150) {
      fidelityDiscount = 15;
      totalPrice -= fidelityDiscount;
      this.customer.fidelityBalance -= 150;
    }
  }

  private String printText(HashMap<String, Play> plays) {

    String result = String.format("Invoice for: %s\n", this.customer.customerName);

    for (Performance perf : this.performances) {
      final Play play = plays.get(perf.playID);
      result += String.format("  %s: %s (%s seats)\n", play.name, frmt.format(play.calculatePrice(perf.audience)), perf.audience);

    }
    result += String.format("Total Amount: %s\n", frmt.format(this.totalPrice));
    result += String.format("%s credits earned.\n", earnedCredits);

    if (this.fidelityDiscount > 0) {
      result += String.format("You earned a discount of %s due to your Fidelity Points!\n", frmt.format(this.fidelityDiscount));
    }
    result +=  String.format("Your Fidelity Points Balance is %s", this.customer.fidelityBalance);
    return result;
  }

  //TODO: ADD PRINT HTML METHOD
}
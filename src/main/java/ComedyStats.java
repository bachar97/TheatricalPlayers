
public class ComedyStats implements StatCalculator {

	@Override
	public Integer calculate(Integer audience) {
		return 30000 + 300 * audience + (audience > 20 ? 10000 + 500 * (audience - 20) : 0);
	}
}

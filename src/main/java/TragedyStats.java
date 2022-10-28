
public class TragedyStats implements StatCalculator{

	@Override
	public Integer calculate(Integer audience) {
		return 40000 + (audience > 30 ? 1000 * (audience - 30) : 0);
	}
}
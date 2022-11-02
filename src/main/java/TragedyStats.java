
public class TragedyStats extends Play{

	public float calculatePrice(int audience) {
		return 400 + (audience > 30 ? 10 * (audience - 30) : 0);
	}

	public int calculateCredits(int audience) {
		return Math.max(audience - 30, 0);
	}
}
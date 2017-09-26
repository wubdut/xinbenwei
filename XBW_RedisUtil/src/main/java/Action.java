
public enum Action {
	BUY(2.0,"b"),SELL(1.0,"s");
	
	private final double value;
	private final String name;
	Action(double value,String name){
		this.value = value;
		this.name = name;
	}
	public double getValue(){
		return this.value;
	}
	
	public String getName(){
		return this.name;
	}
}

public class verdict {
	private String name;
	private String result;
	
	public verdict( String inName, String inResult ) {
		name = inName;
		result = inResult;
	}
	
	public String getName() {
		return name;
	}
	
	public String getResult() {
		return result;
	}
	
	public String toString() {
		return "Result: " + result + " for '" + name + "'.";
	}
}

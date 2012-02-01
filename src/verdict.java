public class verdict {
	private String name;
	private String result;
    private String sClass;
	
	public verdict( String inName, String inResult, String inClass ) {
		name = inName;
		result = inResult;
        sClass = inClass;
	}
	
	public String getName() {
		return name;
	}
	
	public String getResult() {
		return result;
	}
	
	public String toString() {
		return "Result: " + result + "("+sClass+") for '" + name + "'.";
	}
}

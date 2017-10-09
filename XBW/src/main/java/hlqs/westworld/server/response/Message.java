package hlqs.westworld.server.response;

public class Message {
	private String code;
	private String info;
	
	public Message(String code, String info) {
		this.code = code;
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
	public String getCode() {
		return code;
	}
}

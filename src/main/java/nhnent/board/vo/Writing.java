package nhnent.board.vo;

public class Writing {

	private int writingNum;
	private String title;
	private String email;
	private String password;
	private String content;
	private String filePath;

	public Writing() {
	}
	
	public Writing(int writingNum, String title, String email, String password,
			String content, String filePath) {

		this.writingNum = writingNum;
		this.title = title;
		this.email = email;
		this.password = password;
		this.content = content;
		this.filePath = filePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWritingNum() {
		return writingNum;
	}

	public void setWritingNum(int writingNum) {
		this.writingNum = writingNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}

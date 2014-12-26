package nhnent.board.vo;

import java.util.Date;

public class Log {
	private int writingNum;
	private int logNum;
	private Date changeTime;

	public Log() {
	}

	public Log(int writingNum, int logNum, Date changeTime) {
		this.writingNum = writingNum;
		this.logNum = logNum;
		this.changeTime = changeTime;
	}

	public int getWritingNum() {
		return writingNum;
	}

	public void setWritingNum(int writingNum) {
		this.writingNum = writingNum;
	}

	public int getLogNum() {
		return logNum;
	}

	public void setLogNum(int logNum) {
		this.logNum = logNum;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

}

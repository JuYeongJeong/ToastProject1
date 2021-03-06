package nhnent.board.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import nhnent.board.vo.Log;
import nhnent.board.vo.Writing;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



public class BoardDao {

	public static final int DEFAULT_PAGE_VIEW = 1;
	public static final int MAX_PAGE_VIEW = 10;
	public final int FILE_BUFFER_SIZE = 4096;

	public BoardDao() {
	}

	public int addWriting(Map map, SqlSession sqlSession) {

		Writing writing = new Writing();

		writing.setTitle((String) map.get("title"));
		writing.setEmail((String) map.get("email"));
		writing.setPassword((String) map.get("password"));
		writing.setContent((String) map.get("content"));
		writing.setFilePath((String) map.get("filePath"));

		
		int sqlResult = 0; // sqlTest value -> normal value is 2
		sqlResult += sqlSession.insert("BoardMapper.insertWriting", writing);
		int writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");
		sqlResult += sqlSession.insert("BoardMapper.insertLog", writingNum);
		
		return sqlResult;
	}

	public String uploadFile(String basePath, MultipartFile multipartFile) {
		// TODO Auto-generated method stub
		String filePath = basePath + "/upload";

		File file = new File(filePath);
		if (!file.exists())
			file.mkdir();

		OutputStream out = null;
		try {
			filePath += "\\" + multipartFile.getOriginalFilename();
			out = new FileOutputStream(filePath);
			BufferedInputStream bis = new BufferedInputStream(
					multipartFile.getInputStream());
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			int read;
			while ((read = bis.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}

		} catch (IOException ioe) {
			filePath = "upload:false";
		} finally {
			IOUtils.closeQuietly(out);
		}

		return filePath;
	}


	public Map showList(int pageNum,
			SqlSession sqlSession) {
		Map<String,List> map = new HashMap<>();

		List<Writing> writingList = getWritingList(pageNum,
				BoardDao.MAX_PAGE_VIEW, sqlSession);
		map.put("writingList", writingList);

		List<Log> logList = getCurWritingLogList(writingList, sqlSession);
		map.put("logList", logList);

		List<String> pageList = getPageStrList(sqlSession, pageNum);
		map.put("pageList", pageList);

		return map;
	}

	public Map showWriting(int writingNum,
			SqlSession sqlSession) {
		Map map = new HashMap();

		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);
		if(writing == null)
			return map;
		
		map.put("writing", writing);
		
		if (writing.isValidFile()) {
			File file = new File(writing.getFilePath());
			map.put("fileName", file.getName());
		}

		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		map.put("log", log);

		return map;
	}

	public Writing modifyWriting(int writingNum, SqlSession sqlSession) {
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		return writing;
	}

	public Map<String,Object> updateWriting(Map<String, String> map, SqlSession sqlSession) {

		int writingNum = Integer.parseInt(map.get("writingNum"));
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		writing.setTitle(map.get("title"));
		writing.setContent(map.get("content"));
		writing.setFilePath(map.get("filePath"));

		sqlSession.update("BoardMapper.updateWriting", writing);
		sqlSession.insert("BoardMapper.insertLog", writingNum);
		
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("writing", writing);
		
		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		resultMap.put("log", log);
		
		if(writing.isValidFile())
		{
			File file = new File(writing.getFilePath());
			resultMap.put("fileName", file.getName());
		}
		
		return resultMap;
	}

	public Boolean isCorrectPassword(int writingNum, String password,
			SqlSession sqlSession) {
		// TODO Auto-generated method stub
		if (password == null || password.length() == 0)
			return false;

		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		return password.equals(writing.getPassword());
	}

	public Boolean isCorrectEmail(String email) {
		// TODO Auto-generated method stub
		if (email == null)
			return false;

		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		return Pattern.matches(regex, email.trim());

	}
	
//private methods
	private List getWritingList(int pageNum, int limit, SqlSession sqlSession) {
		int endNum = pageNum * 10;
		int startNum = endNum - 9;
		
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		if(startNum > writingCount)
		{
			startNum = 0;
			endNum =0;
		}
		
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("startNum", startNum);
		hashMap.put("endNum", endNum);

		List<Writing> writingList = (ArrayList) sqlSession.selectList(
				"BoardMapper.writingList", hashMap);
		return writingList;
	}

	
	private List getCurWritingLogList(List<Writing> writingList,
			SqlSession sqlSession) {
		List<Log> logList = new ArrayList<>();

		//java8 lamda application
		writingList.forEach(write -> {
			int writingNum = write.getWritingNum();
			Log wLog = sqlSession.selectOne("BoardMapper.curLog", writingNum);
			logList.add(wLog);
		});

		return logList;
	}
	
	private List<String> getPageStrList(SqlSession sqlSession, int pageNum) {
		List<String> pageList = new ArrayList<>();
		if(pageNum < 1)
			return pageList;
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int pageGroupCount = (int) Math.ceil(writingCount / 100.0);
		int pageGroup = (int) Math.ceil(pageNum / 10.0);
		int pageListCount = (int) Math.ceil(writingCount / 10.0);
		if(pageGroup > pageGroupCount)
			return pageList;
	
		int startNum = (pageGroup - 1) * 10 + 1;
		int endNum = pageGroupCount >= pageGroup + 1  ? startNum + 9 : pageListCount; 
		
		if (startNum > 10)
			pageList.add("prev");

		for (int i = startNum; i <= endNum; i++) {
			pageList.add(Integer.toString(i));
		}

		if (endNum < pageListCount)
			pageList.add("next");

		return pageList;
	}

	public int getPageNum(int writingNum, SqlSession sqlSession) {
		// TODO Auto-generated method stub
		int writingRank = sqlSession.selectOne("BoardMapper.writingRank",writingNum);
		return (int) Math.ceil(writingRank/10.0);
	}

	public boolean isValidPageNum(int pageNum, SqlSession sqlSession) {
		// TODO Auto-generated method stub
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int pageNumCount = (int) Math.ceil(writingCount / 10.0);
				
		
		return pageNum <= pageNumCount;
	}

}

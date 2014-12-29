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
	public final int FILE_BUFFER_SIZE = 4084;
	
	public BoardDao() {
	}

	public void addWriting(Map map, SqlSession sqlSession) {

		Writing writing = new Writing();
		
		writing.setTitle((String) map.get("title"));
		writing.setEmail((String)map.get("email"));
		writing.setPassword((String)map.get("password"));
		writing.setContent((String)map.get("content"));
		writing.setFilePath((String)map.get("filePath"));
		
		sqlSession.insert("BoardMapper.insertWriting", map);
		int writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");
		sqlSession.insert("BoardMapper.insertLog", writingNum);
	}

	public String uploadFile(String basePath, MultipartFile multipartFile) {
		// TODO Auto-generated method stub
		String filePath=basePath+"/upload";
		multipartFile.getOriginalFilename();
		
		File file = new File(filePath);
		if(!file.exists())
			file.mkdir();
			
		OutputStream out = null;
		try {
			filePath += "\\"+ multipartFile.getOriginalFilename();
            out = new FileOutputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
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

	public List getWritingList(int pageNum, int limit, SqlSession sqlSession) {
		int endNum = pageNum * 10;
		int startNum = endNum - 9;

		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		hashMap.put("startNum", startNum);
		hashMap.put("endNum", endNum);

		List<Writing> writingList = (ArrayList) sqlSession.selectList(
				"BoardMapper.writingList", hashMap);
		return writingList;
	}

	public List getCurWritingLogList(List<Writing> writingList,
			SqlSession sqlSession) {
		List<Log> logList = new ArrayList<Log>();

		for (Writing write : writingList) {
			int writingNum = write.getWritingNum();
			Log wLog = sqlSession.selectOne("BoardMapper.curLog", writingNum);
			logList.add(wLog);
		}

		return logList;
	}

	public List<String> getPageStrList(SqlSession sqlSession, int pageNum) {
		int pageGroup = (int) Math.ceil(pageNum / 10.0);
		int startNum = (pageGroup - 1) * 10 + 1;
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int endNum = writingCount < startNum + 9 ? writingCount : startNum + 9;

		List<String> pageList = new ArrayList<String>();
		if (startNum > 10)
			pageList.add("prev");

		for (int i = startNum; i <= endNum; i++) {
			pageList.add(Integer.toString(i));
		}

		if (endNum < writingCount)
			pageList.add("next");

		return pageList;
	}

	public Map showList(ModelAndView modelAndView, int pageNum,
			SqlSession sqlSession) {
		Map map = new HashMap();

		List<Writing> writingList = getWritingList(pageNum,
				BoardDao.MAX_PAGE_VIEW, sqlSession);
		map.put("writingList", writingList);

		List<Log> logList = getCurWritingLogList(writingList, sqlSession);
		map.put("logList", logList);

		List<String> pageList = getPageStrList(sqlSession, pageNum);
		map.put("pageList", pageList);

		return map;
	}

	public Map showWriting(ModelAndView modelAndView, int writingNum,
			SqlSession sqlSession) {
		Map map = new HashMap();

		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);
		map.put("writing", writing);

		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		map.put("log", log);

		return map;
	}

	public Writing modifyWriting(int writingNum, SqlSession sqlSession) {
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		return writing;
	}

	public void updateWriting(ModelAndView modelAndView,
			HttpServletRequest request, SqlSession sqlSession) {

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		writing.setTitle((String) request.getParameter("title"));
		writing.setContent((String) request.getParameter("content"));
		writing.setFilePath((String) request.getParameter("filePath"));

		sqlSession.update("BoardMapper.updateWriting", writing);
		sqlSession.insert("BoardMapper.insertLog", writingNum);

		modelAndView.addObject("writing", writing);
		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		modelAndView.addObject("log", log);
	}

	public Boolean isCollectPassword(int writingNum, String password,
			SqlSession sqlSession) {
		// TODO Auto-generated method stub
		if (password == null || password.length() == 0)
			return false;

		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		return password.equals(writing.getPassword());
	}

	public Boolean isCollectEmail(String email, SqlSession sqlSession) {
		// TODO Auto-generated method stub
		if (email == null)
			return false;
		
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		return Pattern.matches(regex, email.trim());

	}

	
	
}

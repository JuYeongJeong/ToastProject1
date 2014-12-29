package nhnent.board.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nhnent.board.vo.Log;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;

public class BoardDao {

	public static final int DEFAULT_PAGE_VIEW = 1;
	public static final int MAX_PAGE_VIEW = 10;

	public BoardDao() {
	}

	public void insert(Writing writing, SqlSession sqlSession) {

		sqlSession.insert("BoardMapper.insertWriting", writing);
		int writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");

		sqlSession.insert("BoardMapper.insertLog", writingNum);
	}

	public ArrayList getWritingList(int pageNum, int limit,
			SqlSession sqlSession) {
		int endNum = pageNum * 10;
		int startNum = endNum - 9;

		HashMap<String, Integer> hashMap = new HashMap();
		hashMap.put("startNum", startNum);
		hashMap.put("endNum", endNum);

		ArrayList<Writing> writingList = (ArrayList) sqlSession.selectList(
				"BoardMapper.writingList", hashMap);
		return writingList;
	}

	public ArrayList getCurWritingLogList(ArrayList<Writing> writingList,
			SqlSession sqlSession) {
		ArrayList<Log> logList = new ArrayList();

		for (Writing write : writingList) {
			int writingNum = write.getWritingNum();
			Log wLog = sqlSession.selectOne("BoardMapper.curLog", writingNum);
			logList.add(wLog);
		}

		return logList;
	}

	public ArrayList<String> getPageStrList(SqlSession sqlSession, int pageNum) {
		int pageGroup = (int) Math.ceil(pageNum / 10.0);
		int startNum = (pageGroup - 1) * 10 + 1;
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int endNum = writingCount < startNum + 9 ? writingCount : startNum + 9;

		ArrayList<String> pageList = new ArrayList<String>();
		if (startNum > 10)
			pageList.add("prev");

		for (int i = startNum; i <= endNum; i++) {
			pageList.add(Integer.toString(i));
		}

		if (endNum < writingCount)
			pageList.add("next");

		return pageList;
	}

	public void showList(ModelAndView modelAndView, int pageNum,
			SqlSession sqlSession) {
		ArrayList<Writing> writingList = getWritingList(pageNum,
				BoardDao.MAX_PAGE_VIEW, sqlSession);
		ArrayList<Log> logList = getCurWritingLogList(writingList, sqlSession);
		ArrayList<String> pageList = getPageStrList(sqlSession, pageNum);

		modelAndView.addObject("writingList", writingList);
		modelAndView.addObject("logList", logList);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("curPage", Integer.toString(pageNum));
	}

	public void showWriting(ModelAndView modelAndView, int writingNum,
			SqlSession sqlSession) {
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);
		modelAndView.addObject("writing", writing);

		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		modelAndView.addObject("log", log);
	}

	public void modifyWriting(ModelAndView modelAndView, int writingNum,
			SqlSession sqlSession) {
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);
		modelAndView.addObject("writing", writing);
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

	public boolean checkPassword(int writingNum, String password,
			SqlSession sqlSession) {
		// TODO Auto-generated method stub
		if (password == null || password.length() == 0)
			return false;

		boolean result = false;
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",
				writingNum);

		if (password.equals(writing.getPassword()))
			result = true;

		return result;
	}

}

package nhnent.board.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nhnent.board.vo.Log;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.servlet.ModelAndView;

public class BoardDao {

	public static final int MAX_PAGE_VIEW = 10;

	public BoardDao() {
	}

	public void insert(Writing writing, SqlSession sqlSession) {

		sqlSession.insert("BoardMapper.insertWriting", writing);
		int writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");

		sqlSession.insert("BoardMapper.insertLog", writingNum);
	}

	public void update(Writing writing) {
		
	}

	public void delete(int writingNum) {
		
	}

	public ArrayList viewWritingList(int pageNum, int limit,
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
	
	public ArrayList getCurWritingLogList(ArrayList<Writing> writingList, SqlSession sqlSession)
	{
		ArrayList<Log> logList = new ArrayList();
		
		for(Writing write : writingList)
		{
			int writingNum = write.getWritingNum();
			Log wLog = sqlSession.selectOne("BoardMapper.curLog", writingNum);
			logList.add(wLog);
		}
		
		return logList;
	}
	

	public int getPageCount(SqlSession sqlSession) {
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int pageCount = writingCount / MAX_PAGE_VIEW ;
		pageCount = (writingCount % MAX_PAGE_VIEW)> 0 ? pageCount + 1 : pageCount;
		
		return pageCount;
	}

	public void showList(ModelAndView modelAndView, int listNum, SqlSession sqlSession) {
		ArrayList<Writing> writingList = viewWritingList(listNum, BoardDao.MAX_PAGE_VIEW, sqlSession);
		ArrayList<Log> logList  = getCurWritingLogList(writingList, sqlSession);
		int pageCount = getPageCount(sqlSession);
		
		modelAndView.addObject("writingList",writingList);
		modelAndView.addObject("logList", logList);
		modelAndView.addObject("pageCount", pageCount);
		
	}

	public void showWriting(ModelAndView modelAndView, int writingNum,
			SqlSession sqlSession) {
		Writing writing = sqlSession.selectOne("BoardMapper.writingView",writingNum);
		modelAndView.addObject("writing",writing);
		
		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		modelAndView.addObject("log",log);
	}
	
	
}

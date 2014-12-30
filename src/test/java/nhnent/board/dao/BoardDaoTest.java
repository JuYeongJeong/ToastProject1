package nhnent.board.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhnent.board.dbutil.DBUtil;
import nhnent.board.vo.Log;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration

public class BoardDaoTest {

	private BoardDao boardDao = new BoardDao();
	private SqlSession sqlSession = null;
	private DBUtil dbUtil = null;

	public BoardDaoTest() {
		dbUtil = DBUtil.getInstance();;
	}

	@Test
	// 글 삽입
	public void testAddWriting() {
		sqlSession = dbUtil.getSession();
		// 글 상입 테스트
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", "테스트");
		map.put("email", "text@text.com");
		map.put("password", "test");
		map.put("content", "test content");

		int result = boardDao.addWriting(map, sqlSession);
		assertEquals(result, 2);
		sqlSession.commit();

		// 삽입 시간 테스트
		int writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");
		Log log = sqlSession.selectOne("BoardMapper.curLog", writingNum);
		assertEquals(log.getChangeTime().getTime(), System.currentTimeMillis(),
				50);

		sqlSession.close();
	}

	@Test
	// 글 목록 보여주기
	public void testShowList() {
		sqlSession = dbUtil.getSession();
		int writingCount = sqlSession.selectOne("BoardMapper.writingCount");
		int writingListSize = 0;
		int logListSize = 0;
		int pageListSize = 0;
		int pageNum =0;
		// 0 page list 요청시-> list들에는 아무 정보도 없다.
		Map<String, List> map = boardDao.showList(pageNum, sqlSession);
		assertEquals(map.get("writingList").size(), writingListSize);
		assertEquals(map.get("logList").size(), logListSize);
		assertEquals(map.get("pageList").size(), pageListSize);

		// 1page 요청시
		pageNum =1;
		map = boardDao.showList(pageNum, sqlSession);
		if (writingCount == 0) {
			writingListSize = 0;
			logListSize = 0;
			pageListSize = 0;
		} else if (writingCount <= 10) {//
			writingListSize = writingCount;
			logListSize = writingCount;
			pageListSize = 1;

		} else if (writingCount > 10) {

			writingListSize = BoardDao.MAX_PAGE_VIEW;
			logListSize = BoardDao.MAX_PAGE_VIEW;
			pageListSize = (int) Math.ceil(writingCount / 10.0);
			pageListSize = pageListSize > 10 ? 11 : pageListSize;

		}
		assertEquals(map.get("writingList").size(), writingListSize);
		assertEquals(map.get("logList").size(), logListSize);
		assertEquals(map.get("pageList").size(), pageListSize);
		
		// 20page 요청시
		pageNum = 20;
		map = boardDao.showList(pageNum, sqlSession);
		if (writingCount == 0) {
			writingListSize = 0;
			logListSize = 0;
			pageListSize = 0;
		} else if (writingCount < 10*(pageNum-1)+1) {//
			writingListSize = 0;
			logListSize = 0;
			pageListSize = 0;
		} else if (writingCount > 10*(pageNum-1)+1) {
			writingListSize = BoardDao.MAX_PAGE_VIEW;
			logListSize = BoardDao.MAX_PAGE_VIEW;
			pageListSize = (int) Math.ceil(writingCount / 10.0);
			pageListSize = pageListSize > pageNum ? 12 : 11;
		}
		assertEquals(map.get("writingList").size(), writingListSize);
		assertEquals(map.get("logList").size(), logListSize);
		assertEquals(map.get("pageList").size(), pageListSize);
	
		sqlSession.close();
	}

	@Test
	// 글 보기 화면
	public void testShowWriting() {
		sqlSession = dbUtil.getSession();
		
		Integer writingNum = 0;
		Writing writing = null;
		Map map = null;

		// 0 writing -> null
		map = boardDao.showWriting(writingNum, sqlSession);
		assertEquals(map.get("writing"), null);

		// recent writing
		writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");
		if (writingNum !=null) {
			map = boardDao.showWriting(writingNum, sqlSession);
			writing = (Writing) map.get("writing");
			assertEquals(writing.getWritingNum(), writingNum.intValue());
		}
		
		sqlSession.close();
	}

	@Test
	public void testUpdateWriting() {
		sqlSession = dbUtil.getSession();
		
		Integer writingNum  = sqlSession.selectOne("BoardMapper.maxWritingNum");
		if (writingNum != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("writingNum",writingNum+"");
			map.put("title","testUpdateWriting");
			map.put("content","content testUpdateWriting");
			boardDao.updateWriting(map, sqlSession);
			
			Writing writing =sqlSession.selectOne("BoardMapper.writingView", writingNum);
			
			assertEquals(writing.getContent(), "content testUpdateWriting");
			assertEquals(writing.getTitle(), "testUpdateWriting");
		}
		
		sqlSession.close();
		
	}

	@Test
	// password 검사
	public void testIsCorrectPassword() {
		sqlSession = dbUtil.getSession();

		Integer writingNum = sqlSession.selectOne("BoardMapper.maxWritingNum");

		if (writingNum != null) {

			// 올바른 비밀 번호
			Writing writing = sqlSession.selectOne("BoardMapper.writingView",
					writingNum);
			assertTrue(boardDao.isCorrectPassword(writingNum,
					writing.getPassword(), sqlSession));

			// 틀린 password
			assertTrue(!boardDao.isCorrectPassword(writingNum,
					writing.getPassword() + "aaa", sqlSession));
		}
		
		sqlSession.close();
	}

	@Test
	// email 유효성
	public void testIsCorrectEmail() {
		sqlSession = dbUtil.getSession();

		// uncorrect email
		assertTrue(!boardDao.isCorrectEmail("111111@aaaaaaa"));
		assertTrue(!boardDao.isCorrectEmail("&&A@aaaaaaa.com"));

		// corrcet email
		assertTrue(boardDao.isCorrectEmail("abc@acb.com"));
		assertTrue(boardDao.isCorrectEmail("a12123bc@acb.com"));
		
		sqlSession.close();
	}

}

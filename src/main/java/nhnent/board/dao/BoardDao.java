package nhnent.board.dao;

import nhnent.board.vo.Log;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;

public class BoardDao {

	public BoardDao() {
	}

	public void insert(Writing writing, SqlSession sqlSession) {
		
		sqlSession.insert("BoardMapper.insertWriting", writing);
		int writingNum = sqlSession.selectOne("BoardMapper.WRITING_NUM");

		sqlSession.insert("BoardMapper.insertLog", writingNum);
	}
	
	

	public void update(Writing writing) {
		
	}

	public void delete(int writingNum) {

	}

	public Writing viewList(int writingNum) {
		Writing writing = null;

		return writing;
	}

}

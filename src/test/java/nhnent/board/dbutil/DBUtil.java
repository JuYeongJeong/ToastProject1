package nhnent.board.dbutil;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBUtil {
	private static DBUtil instance;
	String resource = "nhnent/board/dbutil/SqlMapConfig.xml";
	Reader reader = null;
	SqlSessionFactoryBuilder builder = null;
	SqlSessionFactory factory = null;

	private DBUtil() {
		try {
			// sqlMapConfig parsing
			reader = Resources.getResourceAsReader(resource);
			builder = new SqlSessionFactoryBuilder();
			factory = builder.build(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static DBUtil getInstance() {
		if (instance == null)
			instance = new DBUtil();
		return instance;
	}
	public SqlSession getSession() {
		return factory.openSession();
	}
}

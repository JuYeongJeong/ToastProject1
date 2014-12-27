package nhnent.board.controller;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nhnent.board.dao.BoardDao;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private	BoardDao boardDao = new BoardDao();
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/writingList.jsp");
		
		boardDao.showList(mView, BoardDao.DEFAULT_PAGE_VIEW ,sqlSession);
		
		return mView;
	}
	
	@RequestMapping(value = "/writingView", method = RequestMethod.GET)
	public String writingView(Locale locale, Model model) {
		return "/board/write.jsp";
	}
	
	@RequestMapping(value = "/modifyView", method = RequestMethod.GET)
	public ModelAndView modifyView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/modify.jsp");
		
		int writingNum = Integer.parseInt(request.getParameter("writingNum"));		
		boardDao.modifyWriting(mView, writingNum, sqlSession);
		 
		return mView;
	}
	
	@RequestMapping(value = "/showWriting", method = RequestMethod.GET)
	public ModelAndView showWriting(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/view.jsp");
		
		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		boardDao.showWriting(mView, writingNum, sqlSession);
		
		return mView;
	}
	
	@RequestMapping(value = "/addWriting", method = RequestMethod.POST)
	public ModelAndView addWriting(HttpServletRequest request, HttpServletResponse response ,MultipartFile file) {	
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		Writing r_writing = new Writing();
		
		r_writing.setTitle(multipartRequest.getParameter("title"));
		r_writing.setEmail(multipartRequest.getParameter("email"));
		r_writing.setPassword(multipartRequest.getParameter("password"));
		r_writing.setFilePath(multipartRequest.getParameter("filePath"));
		r_writing.setContent(multipartRequest.getParameter("content"));
		
		boardDao.insert(r_writing, sqlSession);
		
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/showList", method = RequestMethod.GET)
	public ModelAndView showList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/writingList.jsp");
		
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		boardDao.showList(mView,pageNum,sqlSession);
		
		return mView;
	}
	
	@RequestMapping(value = "/updateWriting", method = RequestMethod.POST)
	public ModelAndView updateWriting(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/view.jsp");

		boardDao.updateWriting(mView, request, sqlSession);
		
		return mView;
	}
	
	
	
	
}

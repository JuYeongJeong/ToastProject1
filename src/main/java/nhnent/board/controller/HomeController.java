package nhnent.board.controller;

import java.text.DateFormat;
import java.util.Date;
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
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home.jsp";
	}
	
	@RequestMapping(value = "/writingView", method = RequestMethod.GET)
	public String boardWrite(Locale locale, Model model) {
		logger.info("writeView", locale);
		return "/board/write.jsp";
	}
	
	@RequestMapping(value = "/addWriting", method = RequestMethod.POST)
	public ModelAndView addWriting(HttpServletRequest request, HttpServletResponse response ,MultipartFile file) {
		ModelAndView nextPage = new ModelAndView();
		nextPage.setViewName("/board/writingList.jsp");
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		Writing r_writing = new Writing();
		
		r_writing.setTitle(multipartRequest.getParameter("title"));
		r_writing.setEmail(multipartRequest.getParameter("email"));
		r_writing.setPassword(multipartRequest.getParameter("password"));
		r_writing.setFilePath(multipartRequest.getParameter("filePath"));
		r_writing.setContent(multipartRequest.getParameter("content"));
		
		boardDao.insert(r_writing, sqlSession);

		return nextPage;
	}
	
	
	
	
	
}

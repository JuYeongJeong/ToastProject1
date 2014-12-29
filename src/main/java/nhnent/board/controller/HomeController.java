package nhnent.board.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nhnent.board.dao.BoardDao;
import nhnent.board.vo.Writing;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	private SqlSession sqlSession;
	private BoardDao boardDao = new BoardDao();

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/writingList.jsp");	
		Map map = boardDao.showList(mView, BoardDao.DEFAULT_PAGE_VIEW, sqlSession);
		
		mView.addObject("writingList", map.get("writingList"));
		mView.addObject("logList", map.get("logList"));
		mView.addObject("pageList", map.get("pageList"));
		mView.addObject("curPage", Integer.toString(BoardDao.DEFAULT_PAGE_VIEW));
		
		HttpSession session = request.getSession();
		session.setAttribute("curPage", BoardDao.DEFAULT_PAGE_VIEW);
		
		return mView;
	}

	@RequestMapping(value = "/writingView", method = RequestMethod.GET)
	public String writingView() {
		return "/board/write.jsp";
	}

	@RequestMapping(value = "/modifyView", method = RequestMethod.POST)
	public ModelAndView modifyView(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/modify.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		Writing writing = boardDao.modifyWriting(writingNum, sqlSession);
		mView.addObject("writing", writing);
		return mView;
	}

	@RequestMapping(value = "/showWriting", method = RequestMethod.GET)
	public ModelAndView showWriting(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/view.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		
		Map map = boardDao.showWriting(mView, writingNum, sqlSession);
		mView.addObject("writing", map.get("writing"));
		mView.addObject("log", map.get("log"));
	
		return mView;
	}

	@RequestMapping(value = "/addWriting", method = RequestMethod.POST)
	public ModelAndView addWriting(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		Writing writing = new Writing();

		writing.setTitle(multipartRequest.getParameter("title"));
		writing.setEmail(multipartRequest.getParameter("email"));
		writing.setPassword(multipartRequest.getParameter("password"));
		writing.setFilePath(multipartRequest.getParameter("filePath"));
		writing.setContent(multipartRequest.getParameter("content"));

		boardDao.insert(writing, sqlSession);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/showList", method = RequestMethod.GET)
	public ModelAndView showList(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/writingList.jsp");

		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (Exception e) {
			pageNum = 1;
		}
		Map map = boardDao.showList(mView, pageNum, sqlSession);
		
		mView.addObject("writingList", map.get("writingList"));
		mView.addObject("logList", map.get("logList"));
		mView.addObject("pageList", map.get("pageList"));
		mView.addObject("curPage", Integer.toString(pageNum));
		
		HttpSession session = request.getSession();
		session.setAttribute("curPage", pageNum);

		return mView;
	}

	@RequestMapping(value = "/updateWriting", method = RequestMethod.POST)
	public ModelAndView updateWriting(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/view.jsp");

		boardDao.updateWriting(mView, request, sqlSession);

		return mView;
	}

	@RequestMapping(value = "/isCollectPassword", method = RequestMethod.POST)
	public ModelAndView isCollectPassword(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("/board/emailCheckResult.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		String password = request.getParameter("password");

		Boolean result = boardDao.isCollectPassword(writingNum, password,
				sqlSession);
		mView.addObject("result", result.toString());

		return mView;
	}

}

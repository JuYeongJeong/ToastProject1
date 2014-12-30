package nhnent.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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

	private final int TEN_MB = 10485760;

	@Autowired
	private SqlSession sqlSession;
	
	private BoardDao boardDao = new BoardDao();

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView mView = new ModelAndView("/board/writingList.jsp");

		Map map = boardDao.showList(BoardDao.DEFAULT_PAGE_VIEW,
				sqlSession);

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
		ModelAndView mView = new ModelAndView("/board/modify.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		Writing writing = boardDao.modifyWriting(writingNum, sqlSession);
		mView.addObject("writing", writing);
		return mView;
	}

	@RequestMapping(value = "/showWriting", method = RequestMethod.GET)
	public ModelAndView showWriting(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView("/board/view.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));

		Map map = boardDao.showWriting(writingNum, sqlSession);
		mView.addObject("writing", map.get("writing"));
		mView.addObject("log", map.get("log"));

		String fileName = (String) map.get("fileName");
		if (fileName != null)
			mView.addObject("fileName", fileName);

		return mView;
	}

	@RequestMapping(value = "/addWriting", method = RequestMethod.POST)
	public ModelAndView addWriting(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file) {

		Map map = new HashMap();
		map.put("title", request.getParameter("title"));
		map.put("email",
				request.getParameter("emailFirst") + '@'
						+ request.getParameter("emailSecond"));
		map.put("password", request.getParameter("password"));
		map.put("content", request.getParameter("content"));
		map.put("filePath", request.getParameter("filePath"));

		boardDao.addWriting(map, sqlSession);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/showList", method = RequestMethod.GET)
	public ModelAndView showList(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView("/board/writingList.jsp");

		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (Exception e) {
			pageNum = 1;
		}
		Map map = boardDao.showList(pageNum, sqlSession);

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
		ModelAndView mView = new ModelAndView("/board/view.jsp");
		Map<String,String> map = new HashMap<String,String>();
		map.put("writingNum",request.getParameter("writingNum"));
		map.put("title",request.getParameter("title"));
		map.put("content",request.getParameter("content"));
		
		Map<String,Object> resultMap = boardDao.updateWriting(map, sqlSession);
		mView.addObject("writing",resultMap.get("writing"));
		mView.addObject("log",resultMap.get("log"));
		
		return mView;
	}

	@RequestMapping(value = "/isCorrectPassword", method = RequestMethod.POST)
	public ModelAndView isCorrectPassword(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView("/board/ajaxCheckResult.jsp");

		int writingNum = Integer.parseInt(request.getParameter("writingNum"));
		String password = request.getParameter("password");

		Boolean result = boardDao.isCorrectPassword(writingNum, password,
				sqlSession);
		mView.addObject("result", result.toString());

		return mView;
	}

	@RequestMapping(value = "/isCorrectEmail", method = RequestMethod.POST)
	public ModelAndView isCorrectEmail(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mView = new ModelAndView("/board/ajaxCheckResult.jsp");

		String email = request.getParameter("email");

		Boolean result = boardDao.isCorrectEmail(email);
		mView.addObject("result", result.toString());

		return mView;
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public ModelAndView fileUpload(MultipartHttpServletRequest request) {
		ModelAndView mView = new ModelAndView("/board/ajaxCheckResult.jsp");

		String fileName = request.getFileNames().next();
		MultipartFile multipartFile = request.getFile(fileName);

		if (multipartFile.getSize() > TEN_MB)
			return mView.addObject("result", "upload:overSize");

		String strResult = boardDao.uploadFile(request.getRealPath(""),
				multipartFile);
		mView.addObject("result", strResult);
		return mView;
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void fileDown(HttpServletRequest request,
			HttpServletResponse response) {

		// construct the complete absolute path of the file
		String filePath = request.getParameter("filePath");
		File downloadFile = new File(filePath);

		// set content attributes for the response
		response.setContentType("application/octet-stream");
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		FileInputStream inputStream = null;
		OutputStream outStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
				
			inputStream.close();
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			
		}
	}
}

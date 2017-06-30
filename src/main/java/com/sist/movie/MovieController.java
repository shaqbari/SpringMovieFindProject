package com.sist.movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.dao.MovieDAO;
import com.sist.manager.MovieVO;

@Controller
public class MovieController {
	
	@Autowired
	private MovieDAO dao;
	
	@RequestMapping("main/main.do")
	public String movie_main(String page, Model model){
		if (page==null) {
			page="1";
		}
		int curpage=Integer.parseInt(page);
		
		List<String> gList=dao.movieAllGenre();
		List<MovieVO> mList=dao.movieAllData(curpage);
		int totalpage=dao.movieTotalPage();
		
		System.out.println("totalpage는 "+totalpage);
		
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("curpage", curpage);
		model.addAttribute("mList", mList);
		model.addAttribute("gList", gList);		
		model.addAttribute("main_jsp", "default.jsp");
		return "main/main";
	}
	
	@RequestMapping("main/detail.do")//page는 null이 들어올 수 있으므로 int가아닌 String으로 받아야 한다.
	public String main_detail(int mno, String page, Model model){
		
		
		
		MovieVO vo=dao.movieDetail(mno);
		System.out.println("title은"+vo.getTitle());
		
		model.addAttribute("vo", vo);
		model.addAttribute("page", page);
		model.addAttribute("main_jsp", "detail.jsp");
		return "main/main";
	}
}

package com.sist.movie;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.dao.MovieDAO;
import com.sist.manager.MovieInfoVO;
import com.sist.manager.MovieManager;
import com.sist.manager.MovieVO;

import java.util.*;
@Controller
public class MovieController {
   @Autowired
	private MovieDAO dao;
   @Autowired
   private MovieManager mgr;
   @RequestMapping("main/main.do")
   public String movie_main(String page,Model model)
   {
	   if(page==null)
		   page="1";
	   int curpage=Integer.parseInt(page);
	   List<String> gList=dao.movieAllGenre();
	   List<MovieVO> mList=dao.movieAllData(curpage);
	   List<MovieInfoVO> rList=mgr.cgvMainData();
	   int totalpage=dao.movieTotalPage();
	   model.addAttribute("rList", rList);
	   model.addAttribute("curpage", curpage);
	   model.addAttribute("totalpage", totalpage);
	   model.addAttribute("gList", gList);
	   model.addAttribute("mList", mList);
	   model.addAttribute("main_jsp", "default.jsp");
	   return "main/main";
   }
   @RequestMapping("main/detail.do")
   public String main_detail(int mno,int page,Model model)
   {
	   MovieVO vo=dao.movieDetail(mno);
	   model.addAttribute("vo", vo);
	   model.addAttribute("page", page);
	   model.addAttribute("main_jsp", "detail.jsp");
	   return "main/main";
   }
   @RequestMapping("main/find.do")
   public String main_find(String sb,Model model)
   {
	   List<MovieVO> list=dao.movieFind(sb);
	   model.addAttribute("list", list);
	   model.addAttribute("main_jsp", "find.jsp");
	   return "main/main";
   }
   @RequestMapping("main/genre.do")
   public String main_genre(String data,Model model)
   {
	   List<MovieVO> list=dao.movieGenreFind(data);
	   String json=jsonCreate(list);
	   System.out.println(json);
	   model.addAttribute("json", json);
	   model.addAttribute("list", list);
	   model.addAttribute("genre", data);
	   model.addAttribute("main_jsp", "../genre/genre.jsp");
	   return "main/main";
   }
   public String jsonCreate(List<MovieVO> list)
   {
	   String data="";
	   /*
	    *  {
    		      movieTitle: "Ender's Game",
    		      movieDirector: "Gavin Hood",
    		      movieImage: "https://s3-us-west-2.amazonaws.com/s.cdpn.io/3/movie-endersgame.jpg"
    		    }
	    */
	   try
	   {
		   JSONArray arr=new JSONArray();
		   for(MovieVO vo:list)
		   {
			   JSONObject obj=
					   new JSONObject();
			   obj.put("movieTitle", vo.getTitle());
			   obj.put("movieDirector", vo.getDirector());
			   obj.put("movieImage", vo.getPoster());
			   arr.add(obj);
		   }
		   data=arr.toJSONString();
	   }catch(Exception ex)
	   {
		   System.out.println(ex.getMessage());
	   }
	   return data;
   }
}








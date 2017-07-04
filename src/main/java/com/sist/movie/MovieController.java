package com.sist.movie;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.hadoop.mapreduce.JobRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.dao.MovieDAO;
import com.sist.manager.MovieInfoVO;
import com.sist.manager.MovieManager;
import com.sist.manager.MovieVO;
import com.sist.naver.NaverBlogManager;
import com.sist.r.RManager;
import com.sist.recommand.RecommandManager;

import java.io.*;
import java.util.*;

import javax.annotation.Resource;
@Controller
public class MovieController {
	
	@Autowired
	private Configuration conf; //application-hadoop.xml에서 생성했다. 하둡연결할 주소값을 가지고 있다.
	
	@Autowired
	private NaverBlogManager nbm;
	
   @Autowired
	private MovieDAO dao;
   
   @Autowired
   private MovieManager mgr;
   
/*   @Autowired
   private JobRunner jr;//맵리듀스를 수행하는 클래스
   //같은 클래스 두개쓸때는 id를 명시해 줘야하는데 @Qualifier를 더쓰거나 두개를 합친 @Resource를 쓴다.
*/  
   @Autowired
   @Qualifier("mj")
   private JobRunner jr;//맵리듀스를 수행하는 클래스
   
   @Resource(name="rj")
   private JobRunner recomm;//맵리듀스를 수행하는 클래스
   
   @Autowired
   private RManager rm;
   
   @Autowired
   private RecommandManager rcm;
   
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
	   
	   //1.파일만들기
	   nbm.naverBlogData(vo.getTitle());
	   nbm.naverXmlParse();
	   hadoopFileDelete();
	   copyFromLocal();	   
	   
	   //2.분석
	   try {
		   jr.call(); //맵리듀스 실행요청
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	   //3.결과값 ==> local
	   copyToLocal();	   
	   
	   //4. R(통계)
	   String json=rm.rFeelGraph();
	   model.addAttribute("json", json);
	   model.addAttribute("title", vo.getTitle());
	   
	   //5. 몽고디비에 저장
	   
	   //6. Web에 데이터 전송(그래프)
	   
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
   
   @RequestMapping("main/recommand.do")
   public String main_recommand(String redata, Model model)
   {
	   if (redata==null) {
		redata="더운 날에 볼만한 영화";
	  }else{
		  redata+="에 볼만한 영화";
	  }
	   System.out.println("redata = "+redata);
	   rcm.naverBlogData(redata);
	   rcm.naverXmlParse();
	   
	   recommFileDelete();
	   recommCopyFromLocal();
	   
	   try {
		   recomm.call();
	} catch (Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	   recommCopyToLocal();
	   
	   List<MovieVO> list=rm.recommandData();
	   String json=jsonCreate(list);
	   
	   model.addAttribute("json", json);
	   model.addAttribute("genre", redata);
	   model.addAttribute("main_jsp", "../genre/recommand.jsp");
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
   
   //aop로 아래 작업을 한다.
   //1.폴더 지우기(HDFS)
   public void hadoopFileDelete(){
	 
	   
	  try {
		 //리눅스에서 hadoop fs 명령어에 해당
		FileSystem fs=FileSystem.get(conf);
		if (fs.exists(new Path("/input_movie/naver_ns1.txt"))) {
			// rm -rf  ==> rmr 에 해당
			fs.delete(new Path("/input_movie/naver_ns1.txt"), true);//기존 파일 있으면 삭제해야 한다.
		}
		if (fs.exists(new Path("/output_ns1"))) {
			// rm -rf  ==> rmr 에 해당
			fs.delete(new Path("/output_ns1"), true);//output폴더도 있다면 제거해야 한다.
		}
		fs.close();//쓰고 항상 닫아야 한다.
		
	} catch (Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}  
	   
   }   
   
   //2.파일 올리기
   /*
    * copyFromLocal: 리눅스에서는 이제 안쓰이는 명령어이지만 java에서는 명령어가 안바뀌었다.(appendToFile에 해당) local=>hadoop
    * copyToLocal : hadoop => local
    * */
   public void copyFromLocal(){
	 try {
		 FileSystem fs=FileSystem.get(conf);
		 fs.copyFromLocalFile(new Path("/home/sist/movie_data_ns1/naver.txt"), new Path("/input_movie/naver_ns1.txt"));//local, hadoop
		 fs.close();
		 
	} catch (Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	   
   }
   
   
   public void copyToLocal(){
		try {
			FileSystem fs=FileSystem.get(conf);
			fs.copyToLocalFile(new Path("/output_ns1/part-r-00000"), new Path("/home/sist/movie_data/result"));//mapreduce결과값을 가져온다.
			fs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
   
   
   
   public void recommFileDelete(){
		 
	   
		  try {
			 //리눅스에서 hadoop fs 명령어에 해당
			FileSystem fs=FileSystem.get(conf);
			if (fs.exists(new Path("/input_recom_ns1/recommand_ns1.txt"))) {//경로부분은 매개변수로 받는것이 좋다.
				// rm -rf  ==> rmr 에 해당
				fs.delete(new Path("/input_recom_ns1/recommand_ns1.txt"), true);//기존 파일 있으면 삭제해야 한다.
			}
			if (fs.exists(new Path("/output_recom_ns1"))) {
				// rm -rf  ==> rmr 에 해당
				fs.delete(new Path("/output_recom_ns1"), true);//output폴더도 있다면 제거해야 한다.
			}
			fs.close();//쓰고 항상 닫아야 한다.
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}  
		   
	   }   
	   
	   //2.파일 올리기
	   /*
	    * copyFromLocal: 리눅스에서는 이제 안쓰이는 명령어이지만 java에서는 명령어가 안바뀌었다.(appendToFile에 해당) local=>hadoop
	    * copyToLocal : hadoop => local
	    * */
	   public void recommCopyFromLocal(){
		 try {
			 FileSystem fs=FileSystem.get(conf);
			 fs.copyFromLocalFile(new Path("/home/sist/movie_data/recommand.txt"), new Path("/input_recom_ns1/recommand_ns1.txt"));//local, hadoop
			 fs.close();
			 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		   
	   }
	   
	   public void recommCopyToLocal(){
			try {
				FileSystem fs=FileSystem.get(conf);
				fs.copyToLocalFile(new Path("/output_recom_ns1/part-r-00000"), new Path("/home/sist/movie_data/recommand_result"));//mapreduce결과값을 가져온다.
				fs.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
	  
}








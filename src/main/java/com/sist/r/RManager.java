package com.sist.r;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.dao.MovieDAO;
import com.sist.manager.MovieVO;

@Component
public class RManager {
	
	@Autowired
	private MovieDAO dao;
	
	public String rFeelGraph(){
		String result="[";
		
		try {
			RConnection rc=new RConnection();
			/*rc.voidEval("data<-read.table(\"/home/sist/movie_data/result\")");
			rc.voidEval("png(\"/home/sist/springDev/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/SpringMovieFindProject/main/feel.png\")");
			rc.voidEval("barplot(data$V2, names.arg=data$V1, col=rainbow(10))");
			rc.voidEval("dev.off()");*/
			
			rc.voidEval("data<-read.table(\"/home/sist/movie_data/result\")");
			REXP p=rc.eval("data$V1");//값가져올때는 eval()메소드를 쓴다.
			String[] feel=p.asStrings();
			p=rc.eval("data$V2");
			int[] count=p.asIntegers();
			
			for (int i = 0; i < feel.length; i++) {
				result+="['"+feel[i]+"',"+count[i]+"],";
				
			}
			result=result.substring(0, result.lastIndexOf(","));//맨마지막,를 지운다.
			result+="]";
			
			rc.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public List<MovieVO> recommandData(){
		List<MovieVO> list=new ArrayList<MovieVO>();
		
		try {
			RConnection rc=new RConnection();
			/*rc.voidEval("data<-read.table(\"/home/sist/movie_data/result\")");
			rc.voidEval("png(\"/home/sist/springDev/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/SpringMovieFindProject/main/feel.png\")");
			rc.voidEval("barplot(data$V2, names.arg=data$V1, col=rainbow(10))");
			rc.voidEval("dev.off()");*/
			
			rc.voidEval("data<-read.table(\"/home/sist/movie_data/recommand_result\")");
			rc.voidEval("data1<-data[order(data$V2, decreasing = T), c(\"V1\", \"V2\")]");
			REXP p=rc.eval("data1$V1");//영화제목
			String[] title=p.asStrings();
			
			for (int i = 0; i < 10; i++) {
				MovieVO vo=dao.movieRecommandData(title[i].replace(",", " "));//공백을 ,로 바꾼걸 다시 공백으로 바꾼다.
				list.add(vo);
			}
			
			rc.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
}

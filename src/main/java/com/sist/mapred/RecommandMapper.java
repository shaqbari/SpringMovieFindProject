package com.sist.mapred;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.dao.MovieDAO;

public class RecommandMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	
	/*@Autowired
	private MovieDAO dao; //메모리할당을 하둡에서 했기 때문에 autowired가 적용이 안된다.*/
	//private MovieDAO dao=new MovieDAO();로하면 dao의 mt가 autowired로 되어있어 null이된다. 그러므로 따로 DAO를 만들어야 한다.
	private RecommandDAO dao=new RecommandDAO();
	
	private final IntWritable one=new IntWritable(1); //자를때마다 1씩 증가시킨다.
	private Text result=new Text();
	
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
			List<String> list=dao.movieTitleAllData();
		
			Pattern[] p=new Pattern[list.size()];
			for(int a=0; a<p.length; a++){
				String str=list.get(a);
				p[a]=Pattern.compile(str);// 정규식 문자열을 패턴으로 만들어준다.
			}
			
			Matcher[] m=new Matcher[list.size()];
				for(int a=0; a<m.length; a++){
				
				m[a]=p[a].matcher(value.toString()); //라인(한줄)로 들어온 문자열 값과 비교해서 해당하는게 있다면 매쳐를 만들어준다. 
				while (m[a].find()) {//라인에서 
					result.set(m[a].group());
					context.write(result, one);//누적
				}
			}
	}
	
	
	
}


















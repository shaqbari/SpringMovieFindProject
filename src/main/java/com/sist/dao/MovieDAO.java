package com.sist.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sist.manager.MovieVO;
import java.util.*;

@Repository
public class MovieDAO {
	@Autowired
	private MongoTemplate mt;
	
	public void movieInsert(MovieVO vo){
		mt.insert(vo, "movie");
		
	}
	
	public List<String> movieAllGenre(){
		/*Query query =new Query();
		List<MovieVO> temp=mt.find(query, MovieVO.class, "movie"); //몽고db는 블럭단위로만 가져올 수 있다
		
		//다가져온뒤 장르만 가져오기
		List<String> list=new ArrayList<String>();
		for (MovieVO vo : temp) {
			list.add(vo.getGenre());
		}*/
		
		//아래는 중복된는 거 없이 가져오는 것이다.
		// Select Distinct genre From table과 같다.
		return mt.getCollection("movie").distinct("genre");
	}
	
	public List<MovieVO> movieAllData(int page) {
		//Query query=new Query();
		//List<MovieVO> list=mt.find(query, MovieVO.class, "movie");//이렇게 쓰면 모든데이터 가져온다.
		
		Query query=new Query();
		int rowSize=5;
		int skip=(page-1)*rowSize;// 0부터 시작;
		query.skip(skip).limit(rowSize);
		List<MovieVO> list=mt.find(query, MovieVO.class, "movie");//이렇게 쓰면 모든데이터 가져온다.
			
		return list;
	}
	
	public int movieTotalPage(){
		
		//Select Ceil(count()/5) From table 
		Query query=new Query();
		
		//BasicQuery query=new BasicQuery("{no:1}");//조건이 걸리면 BasicQuery가 편하다.
		return (int) (Math.ceil(mt.count(query, "movie")/5.0));//총 page개수 가져오기
	}
	
	public MovieVO movieDetail(int mno){
		BasicQuery query=new BasicQuery("{mno:"+mno+"}");
		
		return mt.findOne(query, MovieVO.class, "movie");
	}
	
	//영화명으로 검색
	public List<MovieVO> movieFind(String data){
		// find({title:{'$regex':'쇼.*'}})
		
		List<MovieVO> list=new ArrayList<MovieVO>();
		BasicQuery query=new BasicQuery("{title:{'$regex':'.*"+data+"'}");
		list=mt.find(query, MovieVO.class, "movie");
		
		return list;				
	}
}




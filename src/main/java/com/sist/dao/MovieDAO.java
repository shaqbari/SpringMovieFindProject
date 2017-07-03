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
   public void movieInsert(MovieVO vo)
   {
	   mt.insert(vo,"movie");
   }
   public List<String> movieAllGenre()
   {
	   // SELECT DISTINCT genre FROM table
	   return mt.getCollection("movie").
			    distinct("genre");
   }
   public List<MovieVO> movieAllData(int page)
   {
	   Query query=new Query();
	   int rowSize=5;
	   int skip=(page*rowSize)-rowSize;
	   query=query.skip(skip).limit(rowSize);
	   List<MovieVO> list=mt.find(query, 
			   MovieVO.class,"movie");
	   return list;
   }
   public int movieTotalPage()
   {
	   // SELECT CEIL(count()/5) FROM table
	   Query query=new Query();
	   return (int)(Math.ceil(mt.count(query, "movie")/5.0));
   }
   // db.getCollection('movie').find({mno:2})
   public MovieVO movieDetail(int mno)
   {
	   BasicQuery query=new BasicQuery("{mno:"+mno+"}");
	   MovieVO vo=mt.findOne(query, MovieVO.class,"movie");
      return vo;
   }
   public List<MovieVO> movieFind(String data)
   {
	   List<MovieVO> list=new ArrayList<MovieVO>();
	   BasicQuery query=new BasicQuery("{title:{'$regex':'.*"+data+"'}}");
	   list=mt.find(query,MovieVO.class,"movie");
	   return list;
   }
   public List<MovieVO> movieGenreFind(String genre)
   {
	   List<MovieVO> list=new ArrayList<MovieVO>();
	   BasicQuery query=new BasicQuery("{genre:{'$regex':'.*"+genre+"'}}");
	   list=mt.find(query,MovieVO.class,"movie");
	   return list;
   }
   
}







package com.sist.recommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.sist.naver.Item;
import com.sist.naver.NaverBlogManager;
import com.sist.naver.Rss;

@Component
public class RecommandManager {
	public static void main(String[] args) {
		RecommandManager n=new RecommandManager();
		n.naverBlogData("여름에 볼만한 영화");
		n.naverXmlParse();
		System.out.println("저장완료");
		
	}
	
	
    public  void naverBlogData(String title) {
    	//네이버 개발자 검색과, 블로그 권한 가진 계정 필요
        String clientId = "BBCuNu5A_IZlon7H2mVm";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "GJ_NF3YfGE";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(title, "UTF-8");
           // String apiURL = "https://openapi.naver.com/v1/search/blog?query="+ text; // json 결과
            String apiURL = "https://openapi.naver.com/v1/search/blog.xml?display=100&start=1&query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            //하둡안에 바로 못쓰기 때문에 다른 폴더에 파일 만들고 그 xml파일을 parsing한 뒤 parsing한 파일을 하둡으로 보내야 한다.            
            FileWriter fw=new FileWriter("/home/sist/movie_data/recommand.xml");
            fw.write(response.toString());
            fw.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void naverXmlParse(){
    	
    	try {
			File file= new File("/home/sist/movie_data/recommand.xml");
			JAXBContext jc=JAXBContext.newInstance(Rss.class);
			Unmarshaller un=jc.createUnmarshaller();
			//     Object =marshall=>  XML = unmarshall**=> Object 
    		Rss rss=(Rss)un.unmarshal(file);
    		List<Item> list=rss.getChannel().getItem();
    		String data="";
    		for (Item i : list) {
				data+=i.getDescription()+"\n";
			}
    		data=data.substring(0, data.lastIndexOf("\n"));
    		data=data.replaceAll("[^가-힣 ]", "");//한글, 공백빼고 다 지운다.
    		
    		FileWriter fw=new FileWriter("/home/sist/movie_data/recommand.txt");
    		fw.write(data);
    		fw.close();
			
    		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
    }
}

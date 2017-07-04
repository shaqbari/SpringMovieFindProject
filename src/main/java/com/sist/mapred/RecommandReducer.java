package com.sist.mapred;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

// key       values
//액션 [1,1, 1, 1]
public class RecommandReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	private IntWritable res=new IntWritable();

	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		int sum=0;
		String data=key.toString();
		data=data.replace(" ", ",");//공백을 ,로고쳐서 r에 집어넣은뒤 나중에 출력할때 ,를 공백으로 교체한다.
		//data=data+",";
		
		for (IntWritable i : values) {
			sum+=i.get();//IntWritable을 정수형으로 바꾸는 메소드 IntWritable ==> int
			
		}
		key.set(data);
		res.set(sum); // int => IntWritable
		context.write(key, res);
	
	}
	
	
}

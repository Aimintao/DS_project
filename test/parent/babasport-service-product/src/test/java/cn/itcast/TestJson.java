package cn.itcast;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.pojo.SuperPojo;

public class TestJson {

	@Test
	public void testJson1() throws JsonGenerationException, JsonMappingException, IOException {

		// 对象转json
//		Buyer buyer = new Buyer();
//		buyer.setUsername("大毛");
		
		//superpojo 转json
		SuperPojo superPojo = new SuperPojo();
		superPojo.setProperty("username", "damao");
		superPojo.setProperty("password", "123456");
		
		ObjectMapper om = new ObjectMapper();
		
		om.setSerializationInclusion(Include.NON_NULL);
		//Writer w = new StringWriter();
		//om.writeValue(w, buyer);
		
		//将对象转成json
//		String jsonStr = om.writeValueAsString(buyer);
		
		String jsonStr = om.writeValueAsString(superPojo);
		System.out.println(jsonStr);
		
	/*	Buyer buyer2 = om.readValue(jsonStr, Buyer.class);
		System.out.println(buyer2);*/

		SuperPojo readValue = om.readValue(jsonStr, SuperPojo.class);
		System.out.println(readValue.get("username"));
	}

}

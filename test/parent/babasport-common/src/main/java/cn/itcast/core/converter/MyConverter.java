package cn.itcast.core.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 
 * 自定义转换器
 * 
 * @author Administrator
 * @param <S> 转换之前的类型
 * @param <T> 转换之后的类型
 * 
 *
 */
public class MyConverter implements Converter<String, String>{

	//转换的代码
	@Override
	public String convert(String source) {
		
		//字符串去空格，空串变成null
		if(source!=null)
		{
			source = source.trim();
			if(!"".equals(source))
			{
				return source;
			}
		}
		return null;
	}

	
}

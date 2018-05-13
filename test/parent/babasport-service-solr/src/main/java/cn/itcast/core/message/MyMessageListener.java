package cn.itcast.core.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.service.SolrService;

/**
 * 自定义消息监听类
 * 
 * @author Administrator
 *
 */
public class MyMessageListener implements MessageListener {

	@Autowired
	private SolrService solrService;

	@Override
	public void onMessage(Message message) {
		// 当监听到消息之后，可以做事情
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

		try {
			String ids = amessage.getText();
			System.out.println("solr服务接收的ids:" + ids);
			
			String qualifiedName = amessage.getDestination().getQualifiedName();
			System.out.println(qualifiedName);

			solrService.addProduct(ids);

		} catch (JMSException | SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

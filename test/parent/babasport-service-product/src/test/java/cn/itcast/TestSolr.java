package cn.itcast;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * solr测试
 * 
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestSolr {

	/**
	 * 创建索引 （java 传统方式）
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void createIndex1() throws SolrServerException, IOException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer httpSolrServer = new HttpSolrServer(
				"http://192.168.56.101:8080/solr/collection1");

		// 创建文档对象
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id", "1");
		solrInputDocument.addField("name_ik", "范冰冰一号");

		// 添加文档到solr服务器对象
		httpSolrServer.add(solrInputDocument);

		httpSolrServer.commit();// 提交
	}

	@Autowired
	private HttpSolrServer solrServer;

	/**
	 * 创建索引（spring）
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void createIndex2() throws SolrServerException, IOException {

		// 创建文档对象
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id", "2");
		solrInputDocument.addField("name_ik", "范冰冰二号");
		solrServer.add(solrInputDocument);

		solrServer.commit();

	}

}

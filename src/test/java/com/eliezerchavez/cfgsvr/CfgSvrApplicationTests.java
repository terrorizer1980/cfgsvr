package com.eliezerchavez.cfgsvr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CfgSvrApplicationTests {

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate;

	@Autowired
	public CfgSvrApplicationTests(RestTemplateBuilder builder) throws Exception {
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		clientStore.load(new ClassPathResource("ssl/client.p12").getInputStream(), "letmein".toCharArray());
		KeyStore trustStore = KeyStore.getInstance("PKCS12");
		trustStore.load(new ClassPathResource("ssl/truststore.jks").getInputStream(), "changeit".toCharArray());

		SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(clientStore, "letmein".toCharArray())
				.loadTrustMaterial(trustStore, new TrustAllStrategy()).build();

		HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();

		this.restTemplate = new RestTemplateBuilder()
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client)).build();
	}

	@ParameterizedTest
	@ValueSource(strings = { "dev", "stg", "prd" })
	void testRetrieval(String env) throws Exception {
		ResponseEntity<String> response = restTemplate.exchange("https://localhost:" + port + "/http/" + env,
				HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Configuration Server", "Development", "Staging", "Production", "SpringBoot", "SpringCloud" })
	void testEncryptDecrypt(String input) throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(input, null);
		ResponseEntity<String> response = restTemplate.exchange("https://localhost:" + port + "/encrypt",
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());

		HttpEntity<String> entityCipher = new HttpEntity<String>(response.getBody(), null);
		ResponseEntity<String> responseCipher = restTemplate.exchange("https://localhost:" + port + "/decrypt",
				HttpMethod.POST, entityCipher, String.class);

		assertEquals(HttpStatus.OK, responseCipher.getStatusCode());
		assertEquals(input, responseCipher.getBody());
	}

}

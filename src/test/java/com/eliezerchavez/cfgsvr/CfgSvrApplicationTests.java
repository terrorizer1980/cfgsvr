package com.eliezerchavez.cfgsvr;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.ResourceAccessException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CfgSvrApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Autowired
  	private TestRestTemplate restTemplate;

	@Test
	void testBadCertificate() throws Exception {
		assertThrows(ResourceAccessException.class, () -> {
			restTemplate.exchange("/http/dev", HttpMethod.GET, null, String.class);
		});
		
	}

}

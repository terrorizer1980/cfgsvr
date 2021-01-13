import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(classes = BfaConfigServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class BfaConfigServerApplicationTests {

  @Autowired
  protected MockMvc mvc;
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void encryptIT() throws Exception {

    HttpEntity<String> entity = new HttpEntity<String>("PASSWORD", null);
    ResponseEntity<String> response = restTemplate.exchange("/encrypt", HttpMethod.POST, entity, String.class);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
    assertNotNull(response.getBody());
  }

  @Test
  public void decryptErrorIT() throws Exception {

    HttpEntity<String> entity = new HttpEntity<String>("PASSWORD", null);
    ResponseEntity<String> response = restTemplate.exchange("/decrypt", HttpMethod.POST, entity, String.class);

    assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

  @Test
  public void encryptDecryptIT() throws Exception {
    String data = "Data to cypher";
    HttpEntity<String> entityData = new HttpEntity<String>(data, null);
    ResponseEntity<String> responsedata = restTemplate.exchange("/encrypt", HttpMethod.POST, entityData,
            String.class);

    assertEquals(responsedata.getStatusCode(), HttpStatus.OK);
    String cipher = responsedata.getBody();
    assertNotNull(cipher);

    HttpEntity<String> entityCipher = new HttpEntity<String>(cipher, null);
    ResponseEntity<String> responseCipher = restTemplate.exchange("/decrypt", HttpMethod.POST, entityCipher,
            String.class);
    assertEquals(responseCipher.getStatusCode(), HttpStatus.OK);

    assertEquals(data, responseCipher.getBody());

  }
}

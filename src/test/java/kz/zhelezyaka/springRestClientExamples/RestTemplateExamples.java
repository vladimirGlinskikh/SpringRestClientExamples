package kz.zhelezyaka.springRestClientExamples;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateExamples {
    public static final String API_ROOT = "https://api.predic8.de:443/shop";

    @Test
    public void getCategories() {
        String apiUrl = API_ROOT + "/categories/";
        RestTemplate restTemplate = new RestTemplate();
        JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);
        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void getCustomers() {
        String apiUrl = API_ROOT + "/customers/";
        RestTemplate restTemplate = new RestTemplate();
        JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);
        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void createCustomer() {
        String apiUrl = API_ROOT + "/customers/";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("firstname", "Vladimir");
        postMap.put("lastname", "Glinskikh");

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);
        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void updateCustomer() {
        String apiUrl = API_ROOT + "/customers/";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("firstname", "Vladimir");
        postMap.put("lastname", "Glinskikh");

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);
        System.out.println("Response");
        System.out.println(jsonNode.toString());

        String customerUrl = jsonNode.get("customer_url").textValue();
        String id = customerUrl.split("/")[3];
        System.out.println("Created customer id: " + id);

        postMap.put("firstname", "Vladimir 2");
        postMap.put("lastname", "Glinskikh 2");

        restTemplate.put(apiUrl + id, postMap);
        JsonNode updateNode = restTemplate.getForObject(apiUrl + id, JsonNode.class);
        System.out.println(updateNode.toString());
    }

    @Test(expected = ResourceAccessException.class)
    public void updateCustomerUsingPatchSunHttp() {

        String apiUrl = API_ROOT + "/customers/";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("firstname", "Sam");
        postMap.put("lastname", "Axe");

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());

        String customerUrl = jsonNode.get("customer_url").textValue();

        String id = customerUrl.split("/")[3];

        System.out.println("Created customer id: " + id);

        postMap.put("firstname", "Sam 2");
        postMap.put("lastname", "Axe 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(postMap, headers);

        //fails due to sun.net.www.protocol.http.HttpURLConnection not supporting patch
        JsonNode updatedNode = restTemplate.patchForObject(apiUrl + id, entity, JsonNode.class);
        System.out.println(updatedNode.toString());
    }

    @Test
    public void updateCustomerUsingPatch() throws Exception {
        String apiUrl = API_ROOT + "/customers/";

        // Use Apache HTTP client factory
        //see: https://github.com/spring-cloud/spring-cloud-netflix/issues/1777
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("firstname", "Sam");
        postMap.put("lastname", "Axe");

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());

        String customerUrl = jsonNode.get("customer_url").textValue();

        String id = customerUrl.split("/")[3];

        System.out.println("Created customer id: " + id);

        postMap.put("firstname", "Sam 2");
        postMap.put("lastname", "Axe 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(postMap, headers);

        JsonNode updatedNode = restTemplate.patchForObject(apiUrl + id, entity, JsonNode.class);
        System.out.println(updatedNode.toString());
    }

    @Test(expected = HttpClientErrorException.class)
    public void deleteCustomer() {
        String apiUrl = API_ROOT + "/customers/";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("firstname", "Les");
        postMap.put("lastname", "Claypool");

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());

        String customerUrl = jsonNode.get("customer_url").textValue();

        String id = customerUrl.split("/")[3];

        System.out.println("Created customer id: " + id);

        restTemplate.delete(apiUrl + id); //expects 200 status

        System.out.println("Customer deleted");

        //should go boom on 404
        restTemplate.getForObject(apiUrl + id, JsonNode.class);
    }
}

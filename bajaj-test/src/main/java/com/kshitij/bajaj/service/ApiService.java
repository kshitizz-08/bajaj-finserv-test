package com.kshitij.bajaj.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kshitij.bajaj.model.FinalQueryRequest;
import com.kshitij.bajaj.model.WebhookRequest;
import com.kshitij.bajaj.model.WebhookResponse;

@Service
public class ApiService {

    private RestTemplate restTemplate = new RestTemplate();

    public void startProcess() {

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        WebhookRequest request = new WebhookRequest();
        request.setName("John Doe");   
        request.setRegNo("REG12347");  
        request.setEmail("john@example.com");

        ResponseEntity<WebhookResponse> response =
                restTemplate.postForEntity(url, request, WebhookResponse.class);

        WebhookResponse body = response.getBody();

        String webhookUrl = body.getWebhook();
        String token = body.getAccessToken();

        System.out.println("Webhook: " + webhookUrl);
        System.out.println("Token: " + token);

        // Decide Question (Odd/Even)
        String regNo = request.getRegNo();
        String digits = regNo.replaceAll("\\D", "");
        int lastTwo = Integer.parseInt(digits.substring(digits.length() - 2));

        String finalQuery;

        if (lastTwo % 2 == 0) {
            System.out.println("Solving Question 2");
            finalQuery = getQuestion2Query();
        } else {
            System.out.println("Solving Question 1");
            finalQuery = getQuestion1Query();
        }

        sendResult(webhookUrl, token, finalQuery);
    }

    // ✅ FINAL SQL FOR QUESTION 1 (ODD)
    private String getQuestion1Query() {
        return "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1;";
    }

    // (Not needed for you, but kept safe)
    private String getQuestion2Query() {
        return "SELECT 1";
    }

    private void sendResult(String webhookUrl, String token, String query) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        FinalQueryRequest req = new FinalQueryRequest();
        req.setFinalQuery(query);

        HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(req, headers);

        restTemplate.postForEntity(webhookUrl, entity, String.class);

        System.out.println("✅ Submitted Successfully");
    }
}
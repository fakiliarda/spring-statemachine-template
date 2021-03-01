package com.yaf.statemachine.client.iml;

import com.yaf.statemachine.client.HueClient;
import com.yaf.statemachine.config.SecretProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HueClientIml implements HueClient {

    private final SecretProperties secretProperties;

    private final String stateURL = "http://192.168.1.21/api/%s/lights/%s/state";

    private final String requestStateJSON1 = "{\"on\":%s}";

    private final String requestStateJSON2 = "{\"on\":%s," +
            "\"hue\":%s, " +
            "\"bri\":%s}";

    public HueClientIml(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }

    @Override
    public void flickLights() {

        var url1 = String.format(stateURL, secretProperties.getUsername(), 1);
        var url2 = String.format(stateURL, secretProperties.getUsername(), 2);
        var url3 = String.format(stateURL, secretProperties.getUsername(), 3);

        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        String onRequestJson = String.format(requestStateJSON1, true);
        String offRequestJson = String.format(requestStateJSON1, false);

        for (int i = 0; i < 5; i++) {

            restTemplate.put(url1, offRequestJson);
            restTemplate.put(url2, offRequestJson);
            restTemplate.put(url3, offRequestJson);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            restTemplate.put(url1, onRequestJson);
            restTemplate.put(url2, onRequestJson);
            restTemplate.put(url3, onRequestJson);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void changeLightState(int id, boolean on) {

        var url = String.format(stateURL, secretProperties.getUsername(), id);
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format(requestStateJSON1, on);
        restTemplate.put(url, requestJson);
    }

    @Override
    public void changeAllLightStates(boolean on) {

        var url1 = String.format(stateURL, secretProperties.getUsername(), 1);
        var url2 = String.format(stateURL, secretProperties.getUsername(), 2);
        var url3 = String.format(stateURL, secretProperties.getUsername(), 3);

        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format(requestStateJSON1, on);

        restTemplate.put(url1, requestJson);
        restTemplate.put(url2, requestJson);
        restTemplate.put(url3, requestJson);

    }

    @Override
    public void changeLightStateOnAndColor(int lightId, int color) {

            var url = String.format(stateURL, secretProperties.getUsername(), lightId);
            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestJson = String.format(requestStateJSON2, true, color, 255);
            restTemplate.put(url, requestJson);
    }


    @Override
    public void changeAllLightStatesOnAndColor(int color) {

        var url1 = String.format(stateURL, secretProperties.getUsername(), 1);
        var url2 = String.format(stateURL, secretProperties.getUsername(), 2);
        var url3 = String.format(stateURL, secretProperties.getUsername(), 3);

        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format(requestStateJSON2, true, color, 255);

        restTemplate.put(url1, requestJson);
        restTemplate.put(url2, requestJson);
        restTemplate.put(url3, requestJson);

    }

    @Override
    public void changeAllLightStatesOnAndColorAndDim(int color, int brightness) {

        var url1 = String.format(stateURL, secretProperties.getUsername(), 1);
        var url2 = String.format(stateURL, secretProperties.getUsername(), 2);
        var url3 = String.format(stateURL, secretProperties.getUsername(), 3);

        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format(requestStateJSON2, true, color, brightness);

        restTemplate.put(url1, requestJson);
        restTemplate.put(url2, requestJson);
        restTemplate.put(url3, requestJson);

    }


}

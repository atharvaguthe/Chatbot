package com.atharva.controller;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class AiApiController {

  public String callGeminiAPI(String prompt){
    String apiKey = "AIzaSyBOLRJJ7A_b-8oCnxC0X4xf2yZ8oq_3T8o"; 

    StringBuilder response = new StringBuilder();
    try{
      URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
      connection.setDoOutput(true);

      JSONObject jsonRequest = new JSONObject().put("contents", List.of(Map.of("parts",List.of(Map.of("text",prompt + "Give this in 3 to 4 lines")))));

      OutputStream os = connection.getOutputStream();
      byte[] input = jsonRequest.toString().getBytes(StandardCharsets.UTF_8);
      os.write(input,0,input.length);

      int responseCode = connection.getResponseCode();
      InputStream is = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
      response = new StringBuilder();
      String line = reader.readLine();
      while(line != null){
        response.append(line);
      }
    } catch (Exception e){
      e.printStackTrace();
    }

    JSONObject jsonResponse = new JSONObject(response.toString());

    return jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
  }
}

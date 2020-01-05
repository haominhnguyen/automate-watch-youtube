package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.selenium.PlayVideo;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", "hello");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/view_video", method = RequestMethod.POST)
    public String viewVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
        System.out.println(file.getOriginalFilename());

        try {
            sendPOST("http://localhost:8080//view_video_vps", link, duration);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/view_video_vps", method = RequestMethod.POST)
    public String viewVideoVps(HttpServletRequest request) {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
        playVideo(link, duration);
        return "success";
    }

    private String sendPOST(String url, String link, String duration) throws IOException {
        String result = "";
        HttpPost post = new HttpPost(url);
        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("link", link));
        urlParameters.add(new BasicNameValuePair("duration", duration));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {
            result = EntityUtils.toString(response.getEntity());
        }

        return result;
    }

    public void playVideo(String link, String duration) {
        Thread playVideo = new PlayVideo(link, duration);
        playVideo.start();
    }

}

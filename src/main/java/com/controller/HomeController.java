package com.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String viewVideo(HttpServletRequest request) {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
        System.out.println(link);

        Thread playVideo = new PlayVideo(link, duration);
        playVideo.start();

        return "success";
    }

}

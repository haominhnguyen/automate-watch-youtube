package com.controller;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.common.Const;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    public String viewVideo(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
//        System.out.println(file.getOriginalFilename());
//        String fileName = file.getOriginalFilename();
        List<String> data = null;
//        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
//            data = readFileExcel(file);
//        } else if (fileName.endsWith(".txt")) {
//            // TODO:
//        }
//
//        try {
//            for (int i = 0; i < data.size(); i++) {
//                System.out.println("OK. GO!!!!!!!! Run in VPS : "+data.get(i));
//                sendPOST("http://" + data.get(i) + ":8000//view_video_vps", link, duration);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        sendPOST("http://localhost:8000//view_video_vps", link, duration);
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

    public List<String> readFileExcel(MultipartFile file) {
        List<String> listVPS = new ArrayList<>();
        try {
            byte b[] = file.getBytes();
            InputStream ips = new ByteArrayInputStream(b);
            Workbook workbook = WorkbookFactory.create(ips);
            Sheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext() && sheet.getRowSumsRight()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Cell cell = row.getCell(1);
                Pattern pattern = Pattern.compile(Const.IPV4_PATTERN);
                if (Objects.nonNull(cell.getStringCellValue()) && !"".equals(cell.getStringCellValue())) {
                    Matcher matcher = pattern.matcher(cell.getStringCellValue());
                    if (matcher.matches()) {
                        listVPS.add(cell.getStringCellValue());
                    }
                } else {
                    break;
                }
            }
            ips.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("List IP: " + listVPS.size());
        return listVPS;
    }
}

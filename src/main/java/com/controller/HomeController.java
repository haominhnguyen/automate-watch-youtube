package com.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
    public String viewVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
        System.out.println(file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            readFileExcel(file);
        } else if (fileName.endsWith(".txt")) {
            // TODO:
        }

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

    public List<String> readFileExcel(MultipartFile file) {
        List<String> listVPS = new ArrayList<>();
        try {
//            String pathDir = System.getProperty("user.dir");
//            FileInputStream file = new FileInputStream(new File(pathDir + "/" + "data.xlsx"));
            byte b[] = file.getBytes();
            InputStream ips = new ByteArrayInputStream(b);
            Workbook workbook = WorkbookFactory.create(ips);
            //Create Workbook instance holding reference to .xlsx file
            //Get first/desired sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext() && sheet.getRowSumsRight()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIP = row.cellIterator();

                while (cellIP.hasNext()) {
                    // next 1 element
                    cellIP.next();
                    Cell cell = cellIP.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue());
                            break;
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listVPS;
    }
}

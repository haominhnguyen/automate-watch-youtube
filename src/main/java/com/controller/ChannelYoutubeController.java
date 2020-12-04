package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.IOUtils;
import com.model.ChannelModel;
import com.model.YoutubeResponseData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by haotr on 01/12/2020.
 */
@Controller
public class ChannelYoutubeController {

    @RequestMapping(value = "/export_video", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("get_all_video", "get_all_video");
        return "get_all_video";
    }

    @GetMapping("/download/test.xlsx")
    public void downloadCsv(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String channelId = request.getParameter("channelId");
        String maxResult = request.getParameter("maxResult");


        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Template_Export_Youtube_Video.xlsx");
        ByteArrayInputStream stream = getAllVideoFromChannel(channelId, maxResult);
        IOUtils.copy(stream, response.getOutputStream());
    }

    public ByteArrayInputStream getAllVideoFromChannel(String channelId,
                                                       String maxResult) throws IOException {
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(
                        "https://www.googleapis.com/youtube/v3/search?channelId=" + channelId + "&part=snippet,id&order=date&maxResults=" + maxResult + "&key=AIzaSyD9tetV3JOLIAiXOdRUJDAt2cAMpQmJGn0"));
        String rawResponse = request.execute().parseAsString();

        // Convert json to Object data
        ObjectMapper om = new ObjectMapper();
        YoutubeResponseData.Root youtubeResponseData = om.readValue(rawResponse, YoutubeResponseData.Root.class);

        // Write to file excel template
        List<ChannelModel> channelModelList = new ArrayList<>();
        ChannelModel channelModel;

        // collect data for excel
        if (Objects.nonNull(youtubeResponseData) && !CollectionUtils.isEmpty(youtubeResponseData.getItems())) {
            for (YoutubeResponseData.Item item : youtubeResponseData.getItems()) {
                if (Objects.nonNull(item.getId()) && Objects.nonNull(item.getId().getVideoId())
                        && Objects.nonNull(item.getSnippet())
                        && Objects.nonNull(item.getSnippet().getTitle())
                        && Objects.nonNull(item.getSnippet().getTitle())) {

                    channelModel = new ChannelModel();
                    // set data output excel
                    channelModel.setLinkVideo(bindingUrlYoutubeVideo(item.getId().getVideoId()));
                    channelModel.setNameVideo(item.getSnippet().getTitle());
                    channelModel.setNameChannel(item.getSnippet().getChannelTitle());

                    // add to list data
                    channelModelList.add(channelModel);
                }
            }
        } else {
            // Something error
        }
        return ChannelYoutubeController.urlChannelExportToExcel(channelModelList);
    }

    /**
     * Write to output stream.
     *
     * @param channelModels
     * @return excel resource
     * @throws IOException
     * @link https://grokonez.com/spring-framework/spring-boot/excel-file-download-from-springboot-restapi-apache-poi-mysql
     */
    public static ByteArrayInputStream urlChannelExportToExcel(List<ChannelModel> channelModels) throws IOException {
        try {
            String[] COLUMNs = {"No", "Channel", "Name of video", "Link Video", "Name up to MyClip", "ID", "Date", "Note"};
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet("Data Channel");

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (ChannelModel channelModel : channelModels) {

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue(channelModel.getNameChannel());
                row.createCell(2).setCellValue(channelModel.getNameVideo());

                Cell ageCell = row.createCell(3);
                ageCell.setCellValue(channelModel.getLinkVideo());
            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Convert ID to URL
     *
     * @param id
     * @return
     */
    private String bindingUrlYoutubeVideo(String id) {
        return String.format("https://www.youtube.com/watch?v=%s", id);
    }


}

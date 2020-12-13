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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by haotr on 01/12/2020.
 */
@Controller
public class ChannelYoutubeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("index", "index");
        return "index";
    }

    @RequestMapping(value = "/export_video", method = RequestMethod.GET)
    public String autoViewVideo(Model model) {
        model.addAttribute("get_all_video", "get_all_video");
        return "get_all_video";
    }

    @GetMapping("/download_all_video")
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String channelId = request.getParameter("channelId");


        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Template_Export_Youtube_Video.xlsx");
        ByteArrayInputStream stream = getAllVideoFromChannel(channelId);
        IOUtils.copy(stream, response.getOutputStream());
    }

    @GetMapping("/download_all_thumb")
    public void downloadThumbnails(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String channelId = request.getParameter("channelId");

        // get all data from google
        String rawResponse = connectToYoutubeDataAPI(channelId);
        // export data to model.
        List<ChannelModel> channelModels = outputAfterCollectData(rawResponse, channelId);
        for (ChannelModel channelModel : channelModels) {
            // save image thumbnails of video to local
            System.out.println("-----------------------------------------------------");
            System.out.println("Thumb video link: " + channelModel.getThumbnailVideo());
            System.out.println("URL video link: " + channelModel.getLinkVideo());
            System.out.println("-----------------------------------------------------");
            writeImageToLocal(channelModel.getThumbnailVideo(), channelModel.getNameVideo(), channelId);
        }
        System.out.println("\n-----------------------------------------------------");
        System.out.println("Total thumbnails downloaded: " +channelModels.size());
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Get all video from channel
     * @param channelId
     * @return
     * @throws IOException
     */
    private ByteArrayInputStream getAllVideoFromChannel(String channelId) throws IOException {

        // get all data from google
        String rawResponse = connectToYoutubeDataAPI(channelId);
        // export data to model.
        List<ChannelModel> channelModelList = outputAfterCollectData(rawResponse, channelId);

        // write to excel file
        ByteArrayInputStream out = ChannelYoutubeController.urlChannelExportToExcel(channelModelList);

        System.out.println("\nDONE PROCESS. Check file excel output!!!");
        return out;
    }

    /**
     * Connect to youtbe data GET API
     * @param channelId
     * @return
     * @throws IOException
     */
    private String connectToYoutubeDataAPI(String channelId) throws IOException {
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(
                        "https://www.googleapis.com/youtube/v3/search?channelId=" + channelId + "&part=snippet,id&order=date&maxResults=50&key=AIzaSyCvnbQXlcZr2ZgYI_Z8QBDCSHpEpXqWPSM"));
        return request.execute().parseAsString();
    }

    /**
     * Create new URL.
     *
     * @param channelId
     * @return
     * @throws IOException
     */
    private HttpRequest createNewRequestCallToYoutube(String channelId, String nextPageToken) throws IOException {
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        return requestFactory.buildGetRequest(
                new GenericUrl(
                        "https://www.googleapis.com/youtube/v3/search?channelId=" + channelId + "&part=snippet,id&order=date&maxResults=50&key=AIzaSyCvnbQXlcZr2ZgYI_Z8QBDCSHpEpXqWPSM&pageToken=" + nextPageToken + ""));
    }

    /**
     * Output after collect data
     * @param rawResponse
     * @param channelId
     * @return
     * @throws IOException
     */
    private List<ChannelModel> outputAfterCollectData(String rawResponse, String channelId) throws IOException {

        List<ChannelModel> channelModelList = new ArrayList<>();
        // Convert json to Object data
        ObjectMapper om = new ObjectMapper();
        YoutubeResponseData.Root youtubeResponseData = om.readValue(rawResponse, YoutubeResponseData.Root.class);
        // collect data for excel
        collectDataForOutputYoutubeAPI(youtubeResponseData, channelModelList);

        // for case > 50 video
        // check pageToken
        String nextPageToken = youtubeResponseData.getNextPageToken();
        while (!StringUtils.isEmpty(nextPageToken)) {
            HttpRequest requestMoreRecord = createNewRequestCallToYoutube(channelId, nextPageToken);
            String rawResponseCaseMoreRecord = requestMoreRecord.execute().parseAsString();

            YoutubeResponseData.Root responseCaseMoreRecord = om.readValue(rawResponseCaseMoreRecord, YoutubeResponseData.Root.class);
            nextPageToken = responseCaseMoreRecord.getNextPageToken();
            collectDataForOutputYoutubeAPI(responseCaseMoreRecord, channelModelList);
        }

        // output data
        return channelModelList;
    }

    /**
     * Collect data to output client
     *
     * @param youtubeResponseData
     * @param channelModelList
     * @throws IOException
     */
    private void collectDataForOutputYoutubeAPI(YoutubeResponseData.Root youtubeResponseData,
                                                List<ChannelModel> channelModelList) throws IOException {
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
                    if (Objects.nonNull(item.getSnippet().getThumbnails())) {
                        if (Objects.nonNull(item.getSnippet().getThumbnails().getHigh())) {
                            channelModel.setThumbnailVideo(item.getSnippet().getThumbnails().getHigh().getUrl().replace("hqdefault", "maxresdefault"));
                        }
                        if (Objects.isNull(channelModel.getThumbnailVideo())
                                && Objects.nonNull(item.getSnippet().getThumbnails().getMedium())) {
                            channelModel.setThumbnailVideo(item.getSnippet().getThumbnails().getMedium().getUrl().replace("mqdefault", "maxresdefault"));
                        }
                        if (Objects.isNull(channelModel.getThumbnailVideo())
                                && Objects.nonNull(item.getSnippet().getThumbnails().getDefaultVal())) {
                            channelModel.setThumbnailVideo(item.getSnippet().getThumbnails().getDefaultVal().getUrl().replace("default", "maxresdefault"));
                        }
                    }

                    // add to list data
                    channelModelList.add(channelModel);
                }
            }
        }
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

    /**
     * Write image to local
     *
     * @param ulrImage
     * @throws IOException
     */
    private void writeImageToLocal(String ulrImage, String nameVideo, String channelId) throws IOException {
        try {
            URL url = new URL(ulrImage);

            // read image
            BufferedImage image = ImageIO.read(url);

            // resize image
            Image scaledImage = image.getScaledInstance(1280, 720, Image.SCALE_AREA_AVERAGING);

            // create path folder
            Path path = Paths.get(System.getProperty("user.dir") + "/thumbnails/"+ channelId);

            System.out.println(path);

            if (!Files.exists(path)) {
                //java.nio.file.Files;
                Files.createDirectories(path);

                System.out.println("Directory folder is created!");

            }
            // save the resize image aka thumbnail
            ImageIO.write(
                    convertToBufferedImage(scaledImage),
                    "JPEG",
                    new File("\\" + path + "\\" + replaceSpecialCharacter(nameVideo) + ".jpg"));

        } catch (IOException e) {
            URL url = new URL(ulrImage.replace("maxresdefault.jpg","hqdefault.jpg"));

            // read image
            BufferedImage image = ImageIO.read(url);

            // resize image
            Image scaledImage = image.getScaledInstance(1280, 720, Image.SCALE_AREA_AVERAGING);

            // create path folder
            Path path = Paths.get(System.getProperty("user.dir") + "/thumbnails");
            // save the resize image aka thumbnail
            ImageIO.write(
                    convertToBufferedImage(scaledImage),
                    "JPEG",
                    new File("\\" + path + "\\" + replaceSpecialCharacter(nameVideo) + ".jpg"));
            System.out.println("Done video hq size:" +url);
        }
        System.out.println("Done: " + nameVideo);
    }

    /**
     * Convert Image to BufferedImage
     * @param img
     * @return
     */
    private static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bufferedImage;
    }

    /**
     * Replace special character in window!
     *
     * @param name
     * @return
     */
    private static String replaceSpecialCharacter(String name) {
        name = name.replace('|', '_')
                .replace("?", "")
                .replace(':', '-')
                .replace('/', '_')
                .replace('\\', '_')
                .replace('*', '_')
                .replace('<', '_')
                .replace('>', '_')
                .replace('"', '\'');
        return name;
    }
}

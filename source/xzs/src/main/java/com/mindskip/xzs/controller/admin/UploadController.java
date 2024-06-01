package com.mindskip.xzs.controller.admin;


import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.configuration.property.SystemConfig;
import com.mindskip.xzs.service.FileUpload;
import com.mindskip.xzs.service.UserService;
import com.mindskip.xzs.viewmodel.admin.file.UeditorConfigVM;
import com.mindskip.xzs.viewmodel.admin.file.UploadResultVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@RequestMapping("/api/admin/upload")
@RestController("AdminUploadController")
public class UploadController extends BaseApiController {

    private final FileUpload fileUpload;
    private final SystemConfig systemConfig;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String IMAGE_UPLOAD = "imgUpload";
    private static final String IMAGE_UPLOAD_FILE = "upFile";
    private static final String UPLOAD_PATH = "/../";
    private final UserService userService;

    @Autowired
    public UploadController(FileUpload fileUpload, SystemConfig systemConfig, UserService userService) {
        this.fileUpload = fileUpload;
        this.systemConfig = systemConfig;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping("/configAndUpload")
    public Object upload(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals(IMAGE_UPLOAD)) {
            try {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(IMAGE_UPLOAD_FILE);
                long attachSize = multipartFile.getSize();
                String imgName = multipartFile.getOriginalFilename();
                String filePath;
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    filePath = fileUpload.uploadFile(inputStream, attachSize, imgName);
                }
                String imageType = imgName.substring(imgName.lastIndexOf("."));
                UploadResultVM uploadResultVM = new UploadResultVM();
                uploadResultVM.setOriginal(imgName);
                uploadResultVM.setName(imgName);
                uploadResultVM.setUrl(filePath);
                uploadResultVM.setSize(multipartFile.getSize());
                uploadResultVM.setType(imageType);
                uploadResultVM.setState("SUCCESS");
                return uploadResultVM;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            UeditorConfigVM ueditorConfigVM = new UeditorConfigVM();
            ueditorConfigVM.setImageActionName(IMAGE_UPLOAD);
            ueditorConfigVM.setImageFieldName(IMAGE_UPLOAD_FILE);
            ueditorConfigVM.setImageMaxSize(2048000L);
            ueditorConfigVM.setImageAllowFiles(Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp"));
            ueditorConfigVM.setImageCompressEnable(true);
            ueditorConfigVM.setImageCompressBorder(1600);
            ueditorConfigVM.setImageInsertAlign("none");
            ueditorConfigVM.setImageUrlPrefix("");
            ueditorConfigVM.setImagePathFormat("");
            return ueditorConfigVM;
        }
        return null;
    }


    @RequestMapping("/image")
    @ResponseBody
    public RestResponse questionUploadAndReadExcel(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        long attachSize = multipartFile.getSize();
        String imgName = multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String filePath = fileUpload.uploadFile(inputStream, attachSize, imgName);
            userService.changePicture(getCurrentUser(), filePath);
            return RestResponse.ok(filePath);
        } catch (IOException e) {
            return RestResponse.fail(2, e.getMessage());
        }
    }

    @PostMapping("/textbook")
    public RestResponse textBookUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        // 得到文件保存的位置以及新文件名
        File dest = new File(UPLOAD_PATH + fileName);
        // 判断Path是否存在，不存在就创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            // 打印日志
            logger.info("上传成功，当前上传的文件保存在 {}", UPLOAD_PATH + fileName);
            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
            Map map = new HashMap<>();
            map.put("filename",fileName);
            return RestResponse.ok(map);
        } catch (IOException e) {
            logger.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return RestResponse.fail(2, "上传失败");
    }

    @PostMapping("/textbook/remove")
    public RestResponse textBookRemove(@RequestParam String filename){
        File dest = new File(UPLOAD_PATH + filename);
        if(dest.exists()){
            // 上传的文件被保存了
            dest.delete();
            // 打印日志
            logger.info("{}删除成功",filename);
            return RestResponse.ok("删除成功");
        }

        // 待完成 —— 文件类型校验工作
        return RestResponse.fail(2, "删除失败");
    }

    @GetMapping("/textbook/download")
    public void textBookDownload(@RequestParam String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        File dest = new File(UPLOAD_PATH + filename);
        if(dest.exists()){
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) dest.length());
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode( filename , "UTF-8"));

            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dest));) {
                byte[] buff = new byte[1024];
                OutputStream os  = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                logger.error("{}",e);
            }
        }
    }
}

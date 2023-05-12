package com.example.back_example.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.back_example.common.CommonResult;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UploadController {

    @Value("${image-path}")
    public String imgPath;

    @Value("${kind-upload}")
    public String kindPath;

    // 是上传封皮时使用
    @ResponseBody
    @RequestMapping("/upload/upload")
    private CommonResult<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        System.out.println(imgPath);
        CommonResult rs = new CommonResult();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String k = sdf.format(new Date());
            System.out.println(k);
            file.transferTo(new File(imgPath+ File.separator + k + originalFilename));
            rs.setCode(0);
            rs.setMsg("上传成功");
            rs.setData( k + originalFilename);
        }catch (IOException e){
            e.printStackTrace();
            rs.setCode(-1);
            rs.setMsg("上传失败" + e.getMessage());
            return rs;
        }
        return rs;
    }

    // 再kindeditor中上传图像使用
    @PostMapping("/upload/kinduploadimg")
    private void kindUploadImg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        PrintWriter writer = response.getWriter();
        // 文件保存目录路径
        String savePath = kindPath;

        String saveUrl = "/kind/upload/";

        // 定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");

        // 最大文件大小
        long maxSize = 1000000;
        response.setContentType("text/html; charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(request)) {
            writer.println(getError("请选择文件。"));
            return;
        }

        File uploadDir = new File(savePath);
        // 判断文件夹是否存在,如果不存在则创建文件夹
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 检查目录写权限
        if (!uploadDir.canWrite()) {
            writer.println(getError("上传目录没有写权限。"));
            return;
        }

        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }
        if (!extMap.containsKey(dirName)) {
            writer.println(getError("目录名不正确。"));
            return;
        }

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        String fileName = null;
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            fileName = mFile.getOriginalFilename();
            // 检查文件大小
            if (mFile.getSize() > maxSize) {
                writer.println(getError("上传文件大小超过限制。"));
                return;
            }
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
                writer.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
                return;
            }
            UUID uuid = UUID.randomUUID();
            String path = savePath + uuid.toString() + "." + fileExt;
            saveUrl = saveUrl + uuid.toString() + "." + fileExt;

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            FileCopyUtils.copy(mFile.getInputStream(), outputStream);


            JSONObject obj = new JSONObject();
            obj.put("error", 0);
            obj.put("url", saveUrl);
            writer.println(JSONObject.toJSONString(obj));

        }
    }
    private String getError(String message) {
        JSONObject obj = new JSONObject();
        obj.put("error", 1);
        obj.put("message", message);
        return JSONObject.toJSONString(obj);
    }
}

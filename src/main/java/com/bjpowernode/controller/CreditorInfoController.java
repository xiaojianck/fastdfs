package com.bjpowernode.controller;


import com.bjpowernode.model.CreditorInfo;
import com.bjpowernode.service.CreditorInfoService;
import com.bjpowernode.utils.FastDFS;
import com.bjpowernode.utils.FastDFS1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Author:箫剑
 * 2019/8/7
 */
@Controller
public class CreditorInfoController {
    @Autowired
    private CreditorInfoService creditorInfoService;

    @GetMapping("/fastdfs/index")
    public String index(Model model){
        List<CreditorInfo> creditorInfoList = creditorInfoService.getAllCreditorInfo();
        model.addAttribute("creditorInfoList",creditorInfoList);
        //模板页面，不是jsp
        return "index";
    }

    @GetMapping("/fastdfs/toUpload")
    public String toUpload(Model model,@RequestParam("id") Integer id){
        model.addAttribute("id",id);
        return "upload";
    }
    @PostMapping("/fastdfs/upload")
    public @ResponseBody String upload(@RequestParam("id") Integer id, @RequestParam("fileName") MultipartFile file){
        //原来文件上传是将文件写到本地或者远程服务器的某个目录下
        //现在的文件上传是将文件上传到fastdfs文件服务器上
        //1表示上传失败  0表示成功
        int result = 1;
        //abc.txt -->txt
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
        try {
            String[] uploadArray = FastDFS.fileUpload(file.getBytes(),fileExt);
            if(uploadArray != null && uploadArray.length ==2){
                //文件上传到fastDFS成功  ,将合同文件路径更新到债权记录中
                CreditorInfo creditorInfo = new CreditorInfo();
                creditorInfo.setId(id);
                creditorInfo.setGroupname(uploadArray[0]);
                creditorInfo.setRemotefilepath(uploadArray[1]);
                int updateRow = creditorInfoService.updateCreditorInfo(creditorInfo);
                //数据库更新成功
                if(updateRow > 0){
                    result = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<script>window.parent.uploadOK('"+result+"')</script>";
    }
    @GetMapping("/fastdfs/download")
    @ResponseBody

    public ResponseEntity<byte[]> download(@RequestParam("id") Integer id){
        //根据债权id获取 债权对象
        CreditorInfo creditorInfo = creditorInfoService.getCreditorInfoById(id);
        String extName = creditorInfo.getRemotefilepath().substring(creditorInfo.getRemotefilepath().indexOf("."));
        byte [] fileBytes = FastDFS1.fileDownload(creditorInfo.getGroupname(),creditorInfo.getRemotefilepath());


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);//流类型
        httpHeaders.setContentDispositionFormData("attachment",System.currentTimeMillis() + extName);

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(fileBytes,httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("/fastdfs/fileDelete")
    public @ResponseBody String fileDelete(@RequestParam("id") Integer id){
        int result = 1;
        try {
            result = creditorInfoService.deleteContract(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }

}

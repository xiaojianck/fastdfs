package com.bjpowernode.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * Author:箫剑
 * 2019/8/7
 */
public class FastDFS1 {
    private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;


    public static StorageClient getStorageClient() throws IOException, MyException {
        //1.加载配置文件，默认去classpath下加载
        ClientGlobal.init("fdfs_client.conf");
        //2.创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //3.创建TrackerServer对象
        trackerServer = trackerClient.getConnection();
        //4.创建StorageServler对象
        storageServer = trackerClient.getStoreStorage(trackerServer);
        //5.创建StorageClient对象，这个对象完成对文件的操作
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        return storageClient;
    }

    public static void closeFastDFS() {
        if (storageServer != null) {
            try {
                storageServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fileUpload() {
        try {
            //1. 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //2.上传文件  第一个参数：本地文件路径 第二个参数：上传文件的后缀 第三个参数：文件信息
            String[] uploadArray = storageClient.upload_file("C:/2.jpg", "jpg", null);
            for (String str : uploadArray) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            closeFastDFS();
        }
    }

    //下载文件的方法
    public static byte[] fileDownload(String group,String remoteFile) {
        byte[] fileBytes=null;
        try {
            //1. 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //2.下载文件 返回0表示成功，其它均表示失败
            fileBytes = storageClient.download_file(group,
                    remoteFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            closeFastDFS();
        }
        return fileBytes;
    }

    //删除文件的方法
    public static int fileDelete(String group, String remoteFile) {
        int num=1;
        try {
            //1. 获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //2.删除文件 返回0表示成功，其它均表示失败
             num = storageClient.delete_file(group,
                    remoteFile);
            System.out.println(num);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            closeFastDFS();
        }
        return num;
    }


    public static void main(String[] args) {
        fileUpload();
        // fileDownload();
        //fileDelete();
    }

}

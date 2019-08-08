package com.bjpowernode.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * Author:箫剑
 * 2019/8/7
 */
public class FastDFS {
    public static void main(String[] args) {

    }

    public static String[] fileUpload(byte[] bytes, String fileExt) {
        TrackerServer trackerServer=null;
        StorageServer storageServer=null;
        String[] uploadArray=null;
        try {

            ClientGlobal.init("fdfs_client.conf");

            TrackerClient trackerClient=new TrackerClient();
            trackerServer=trackerClient.getConnection();
            storageServer=trackerClient.getStoreStorage(trackerServer);
            StorageClient storageClient=new StorageClient(trackerServer,storageServer);
           uploadArray=storageClient.upload_file(bytes, fileExt, null);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return uploadArray;
    }

}

package com.fanger.util;

import org.apache.commons.fileupload.util.Streams;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 文件下载Util
 */
public abstract class RemoteDownload {

    final static org.slf4j.Logger logger= LoggerFactory.getLogger(RemoteDownload.class);
    /**
     * @param path：文件保存的路径。文件路径
     * @param fileurl：文件的url
     * @param flag：true。文件显示在当前保存的路径下，false。文件显示在文件解压后的路径下
     */
    public void  downloadFile(File path,String fileurl,boolean flag){
        try {
            URL url = new URL(fileurl);
            InputStream is = url.openStream();
            final long total = url.openConnection().getContentLength();
            File dir = path;
            if(!dir.exists()){
                dir.mkdirs();
            }
            String fileName = fileurl.substring(fileurl.lastIndexOf("/"));
            File file = new File(dir,fileName);
            byte[] buf = new byte[1024*8];
            int len = 0;
            FileOutputStream fos = null;
            try{
                long sum = 0;
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1){
                    sum += len;
                    fos.write(buf, 0, len);
                    final long finalSum = sum;
                    onProgress(finalSum * 100.0f / total, total);
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                try{
                    if (is != null) is.close();
                } catch (IOException e){
                }
                try{
                    Streams.copy(is, fos, true);
                } catch (IOException e){
                }
            }
            File zipFilePath = new File(dir.toString()+"/"+fileName);
            String zipFileName = fileName.substring(0,fileName.lastIndexOf("."));
            File destDirectory = new File(dir.toString()+"/"+zipFileName);
            if(flag){
                destDirectory = new File(dir.toString());
            }
            logger.info("解压路径："+destDirectory);
            UnzipUtility.unzip(zipFilePath.toString(),destDirectory.toString());
            logger.info("解压完成");
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * 下载进度监听
     * @param progress
     * @param total
     */
    public abstract void onProgress(float progress, long total);
}

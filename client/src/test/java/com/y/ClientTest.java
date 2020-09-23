package com.y;

import com.y.client.Client;
import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import com.y.common.command.upload.UploadResponseCommand;
import com.y.common.exception.CommonException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientTest {


    @Test
    public void loopUploadTest() throws Exception {
        new Client("localhost:8093").init();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            File file = new File("D:\\zlz\\bs923\\bimfileserver\\client\\src\\main\\java\\com\\y\\client\\Client.java");
            UploadResponseCommand uploadResponseCommand = Client.upload(file);
            System.out.println(uploadResponseCommand);
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1 - l);

    }


    @Test
    public void uploadAndDownloadTest() throws Exception {
        new Client("localhost:8093").init();
        for (int i = 0; i < 20; i++) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        long l = System.currentTimeMillis();
                        File file = new File("D:\\zlz\\bs923\\bimfileserver\\client\\src\\main\\java\\com\\y\\client\\Client.java");
                        UploadResponseCommand uploadResponseCommand = Client.upload(file);
                        System.out.println(uploadResponseCommand);
                        long l1 = System.currentTimeMillis();
                        System.out.println(l1 - l);

                        DownloadResponseCommand downloadResponseCommand = Client.download(uploadResponseCommand.getFileId());
                        System.out.println(downloadResponseCommand);
                        File downloadFile = new File("D:\\zlz\\bs923\\bimfileserver\\example\\src\\main\\resources\\download.java");
                        if (downloadFile.exists()) downloadFile.delete();
                        downloadFile.createNewFile();
                        FileUtils.writeByteArrayToFile(downloadFile, downloadResponseCommand.getFileContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        TimeUnit.SECONDS.sleep(30);
    }
}

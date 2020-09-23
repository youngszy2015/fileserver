package com.y.server.file;

import com.y.common.command.upload.UploadRequestCommand;
import com.y.server.config.Config;
import com.y.server.exception.ServerException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Young
 * @Date 2020-9-22 21:04
 */
public class FileManager {

    private final static AtomicBoolean started = new AtomicBoolean(false);

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);


    private static ThreadLocal<Random> FILE_RANDOM = new ThreadLocal<>();


    public static void init() throws ServerException {
        //生成文件保存目录以及目录下的子文件夹
        File file = new File(Config.getFileDir());
        if (!file.exists()) {
            try {
                boolean parentDir = file.mkdir();
            } catch (Exception e) {
                throw new ServerException("create file err,path:" + Config.getFileDir(), e);
            }

            /**
             * 父目录下生成255个子目录
             */
            for (int j = 0; j < 256; j++) {
                String dirName = "" + j;
                File subFile = new File(Config.getFileDir() + "/" + dirName);
                try {
                    subFile.mkdir();
                } catch (Exception e) {
                    throw new ServerException("create file err,path:" + subFile, e);
                }
            }
        } else {
            logger.info("config path exists: {}", Config.getFileDir());
        }
        started.compareAndSet(false, true);
    }

    public static String flushFile(UploadRequestCommand uploadRequestCommand) throws ServerException {
        if (!started.get()) {
            throw new ServerException("file manager not started");
        }
        //生成文件id
        Random random = FILE_RANDOM.get();
        if (null == random) {
            random = new Random();
            FILE_RANDOM.set(random);
        }
        Random selfRandom = FILE_RANDOM.get();
        int r = selfRandom.nextInt(256);
        String fileId = "/" + r + "/" + UUID.randomUUID().toString().replaceAll("-", "");
        //存储文件
        String filePath = Config.getFileDir() + fileId + "." + uploadRequestCommand.getFileSuffix();
        File file = new File(filePath);
        try {
            if (file.exists()) file.delete();
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, uploadRequestCommand.getFileContent());
        } catch (Exception e) {
            throw new ServerException("create new file err: ", e);
        }
        return fileId + "." + uploadRequestCommand.getFileSuffix();
    }


    public static byte[] getFile(String fileId) throws ServerException {
        if (!started.get()) {
            throw new ServerException("file manager not started");
        }
        File file = new File(Config.getFileDir() + fileId);
        if (file.exists()) {
            try {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                return bytes;
            } catch (IOException e) {
                throw new ServerException("read file " + fileId + " err:", e);
            }
        } else {
            return null;
        }
    }
}

package org.sec.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.sec.start.Application;

/** 文件操作*/
public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class);
    private static final boolean DEBUG = true;
    /** 返回绝对路径*/
    public static String getFilePath(String relativePath) {
        String dir = FileUtils.class.getResource("/").getPath();
        return dir + relativePath;
    }

    /** 返回字节码的路径*/
    public static String getFilePath(Class<?> clazz, String className) {
        String path = clazz.getResource("/").getPath();
        return String.format("%s%s.class", path, className.replace('.', File.separatorChar));
    }

    /** 读取Bytes*/
    public static byte[] readBytes(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("[INFO] [org.sec.utils.FileUtils] File Not Exist: " + filepath);
        }

        InputStream in = null;

        try {
            in = new FileInputStream(file);
            in = new BufferedInputStream(in);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtils.copy(in, bao);

            return bao.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(in);
        }

        throw new RuntimeException("[Waring] [org.sec.utils.FileUtils] Can not read file: " + filepath);
    }

    /** 写入Bytes*/
    public static void writeBytes(String filepath, byte[] bytes) {
        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        try (OutputStream out = new FileOutputStream(filepath);
             BufferedOutputStream buff = new BufferedOutputStream(out)) {
            buff.write(bytes);
            buff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (DEBUG) logger.info("file://" + filepath);
    }

    /** 读文件*/
    public static List<String> readLines(String filepath) {
        return readLines(filepath, "UTF8");
    }

    /** 读文件*/
    public static List<String> readLines(String filepath, String charsetName) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException("[Waring] [org.sec.utils.FileUtils] File Not Exist: " + filepath);
        }

        InputStream in = null;
        Reader reader = null;
        BufferedReader bufferReader = null;

        try {
            in = new FileInputStream(file);
            reader = new InputStreamReader(in, charsetName);
            bufferReader = new BufferedReader(reader);

            List<String> list = new ArrayList<>();
            String line;
            while ((line = bufferReader.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(bufferReader);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        assert !DEBUG : "[Waring] [org.sec.utils.FileUtils] bytes is null";

        return null;
    }

    /** 将多行写到文件*/
    public static void writeLines(String filepath, List<String> lines) {
        if (lines == null || lines.size() < 1) return;

        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        OutputStream out = null;
        Writer writer = null;
        BufferedWriter bufferedWriter = null;

        try {
            out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);

            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(bufferedWriter);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(out);
        }
    }

    /** 创建目录*/
    public static void mkdirs(File dirFile) {
        boolean file_exists = dirFile.exists();

        if (file_exists && dirFile.isDirectory()) {
            return;
        }

        if (file_exists && dirFile.isFile()) {
            throw new RuntimeException("[Waring] [org.sec.utils.FileUtils] Not A Directory: " + dirFile);
        }

        if (!file_exists) {
            boolean flag = dirFile.mkdirs();
            assert !DEBUG || flag : "[Waring] [org.sec.utils.FileUtils] Create Directory Failed: " + dirFile.getAbsolutePath();
        }
    }

    /** 删除文件*/
    public static void clear(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delete(f);
                }
            }
        }
        else {
            delete(file);
        }
    }

    /** 删除文件*/
    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            boolean flag = file.delete();
            assert !DEBUG || flag : "[Warning] [org.sec.utils.FileUtils] delete file failed: " + file.getAbsolutePath();
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delete(f);
                }
            }

            boolean flag = file.delete();
            assert !DEBUG || flag : "[Warning] [org.sec.utils.FileUtils] delete file failed: " + file.getAbsolutePath();
        }
    }

    /** 读文件流*/
    public static byte[] readStream(final InputStream in, final boolean close) {
        if (in == null) {
            throw new IllegalArgumentException("[Waring] [org.sec.utils.FileUtils] inputStream is null!!!");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            return out.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (close) {
                IOUtils.closeQuietly(in);
            }
        }
        return null;
    }

    /** 获取输入流*/
    public static InputStream getInputStream(String className) {
        return ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class");
    }
}
package util;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import proto.HyperCube;
import proto.Raft;
import proto.Util;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

public class HyperUtil {

/*
    public static String getHyperCubeParentDir(){
        return System.getProperty("user.home");
    }
    */
    public static String getHyperCubeParentDir(){
        return Paths.get("").toAbsolutePath().toString();
    }

    public String getLogFullPath(){
        return null;
    }
    public static String getCubeFileFullPath(long startIndex, long endIndex){
        return  HyperUtil.getCubeDirPath()+
                File.separator +
                String.format("%015d-%015d.cube",startIndex,endIndex);
    }

    public static String getCubeFileName(long startIndex){
        return String.format("%015d-%015d.cube",startIndex,0);
    }
    public static String getCubeDirPath(){
        return  getHyperCubeParentDir() + File.separator+ "HyperCube";

    }
    public static String getMetaDirPath(){
        return getCubeDirPath()+ File.separator + "meta";
    }


    public static RandomAccessFile openFile(String dir, String fileName, String mode) {
        try {
            String fullFileName = dir + File.separator + fileName;
            File file = new File(fullFileName);
            return new RandomAccessFile(file, mode);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("file not found, file=" + fileName);
        }
    }

    public static String getMetaFileFullPath(){
        return getMetaDirPath() + File.separator + "meta";
    }

    public static HyperCube.HyperResponse buildSuccessResponse(){


        return HyperCube.HyperResponse.newBuilder().setResCode(
                Util.ResCode.SUCCESS
        ).setMsg(Util.ResMessage.newBuilder().setMsg("SUCCESS").build()).build();
    }

    public static HyperCube.HyperResponse buildVerbosFailResponse(
            String where, String detail, String serverId, int port
    ){
        Util.ResMessage newVerboseMessage =
                Util.ResMessage.newBuilder()
                .setMsg("At "+where +" [ "+detail+" ]"+"\r\n"+
                        " server information "+serverId +":"+port).build();

        return HyperCube.HyperResponse.newBuilder()
                .setResCode(Util.ResCode.FAIL)
                .setMsg(newVerboseMessage).build();
    }

    public static HyperCube.HyperResponse buildFailResponse(){
        return HyperCube.HyperResponse.newBuilder()
                .setResCode(Util.ResCode.FAIL)
                .build();
    }

    public static Raft.Node buildSender(String server_id, int port){
        return Raft.Node.newBuilder().setServerId(server_id).setPort(port).build();
    }


    public static  <T extends GeneratedMessageV3> void writeProtoToFile(RandomAccessFile raf, T message) {
        byte[] messageBytes = message.toByteArray();
        long crc32 = getCRC32(messageBytes);
        try {
            raf.writeLong(crc32);
            raf.writeInt(messageBytes.length);
            raf.write(messageBytes);
        } catch (IOException ex) {
            throw new RuntimeException("write proto to file error");
        }
    }

    public static long getCRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }


    public static List<String> getSortedFilesInDirectory(
            String dirName, String rootDirName, boolean fullPath) throws IOException {
        List<String> fileList = new ArrayList<>();
        File rootDir = new File(rootDirName);
        File dir = new File(dirName);
        if (!rootDir.isDirectory() || !dir.isDirectory()) {
            return fileList;
        }
        String rootPath = rootDir.getCanonicalPath();
        if (!rootPath.endsWith("/")) {
            rootPath = rootPath + "/";
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(getSortedFilesInDirectory(file.getCanonicalPath(), rootPath,fullPath));
            } else {
                if(fullPath){
                    fileList.add(file.getCanonicalPath());
                }else{
                    fileList.add(file.getCanonicalPath().substring(rootPath.length()));
                }

            }
        }
        Collections.sort(fileList);
        return fileList;
    }

    /*
    public static List<String> getSortedFileFullPathInDirectory(){
    }
    */


    public static <T extends GeneratedMessageV3> T readProtoFromFile(RandomAccessFile raf, Class<T> clazz) {
        try {
            long crc32FromFile = raf.readLong();
            int dataLen = raf.readInt();
            int hasReadLen = (Long.SIZE + Integer.SIZE) / Byte.SIZE;
            if (raf.length() - hasReadLen < dataLen) {
                return null;
            }
            byte[] data = new byte[dataLen];
            int readLen = raf.read(data);
            if (readLen != dataLen) {
                return null;
            }
            long crc32FromData = getCRC32(data);
            if (crc32FromFile != crc32FromData) {
                return null;
            }
            Method method = clazz.getMethod("parseFrom", byte[].class);
            T message = (T) method.invoke(clazz, data);
            return message;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void deleteAllCubeFiles(){
        //getCubeDirPath()

        File dir = new File(getCubeDirPath());

        for(File file : dir.listFiles()){
            if(!file.isDirectory()){
                file.delete();
            }
        }

    }
}


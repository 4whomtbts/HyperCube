package util;

import com.google.protobuf.util.JsonFormat;
import proto.HyperCube;
import proto.Raft;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class Config {

    private static HyperCube.Config config;
    private static String currentPath;
    private static String configFilePath;
    public static URL getDefaultFeaturesFiles(int index){
        String configName = "HyperConfig.proto"+index+".json";
        System.out.println("Config File Name : "+configName);
        try {
            return new File("D:\\desktop\\naver_d2_fest\\hyper_cube\\src\\main\\resources\\HyperConfig"+index+".json").toURI()
                    .toURL();
        }catch (Exception e){
            return null;
        }
    }


    public static HyperCube.Config getConfig() {
        return config;
    }

    public static String getCurrentPath(){
        return Paths.get("").toAbsolutePath().toString();
    }
    public static HyperCube.Config parseConfig(String configFileName) throws IOException {

        Config.currentPath = HyperUtil.getCubeDirPath();
        Config.configFilePath = currentPath + File.separator + configFileName;
        URL path = new File(configFilePath).toURL();

        InputStream input = path.openStream();

        try {
            Reader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
            try {
                HyperCube.Config.Builder config = HyperCube.Config.newBuilder();
                JsonFormat.parser().merge(reader, config);
                Config.config = config.build();
                return config.build();
            } finally {
                reader.close();
            }

        }finally {
            input.close();
        }
    }



}

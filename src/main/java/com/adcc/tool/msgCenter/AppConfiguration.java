package com.adcc.tool.msgCenter;

import com.adcc.tool.msgCenter.util.LogUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;

/**
 * 配置文件
 */
public class AppConfiguration {

    private static AppConfiguration instance = null;

    private String AOMIPAdapterUrl;
    
    public String getAOMIPAdapterUrl() {
		return AOMIPAdapterUrl;
	}

	public void setAOMIPAdapterUrl(String aOMIPAdapterUrl) {
		AOMIPAdapterUrl = aOMIPAdapterUrl;
	}

	/**
    * 构造方法
    * */
    private AppConfiguration() {

    }

    /**
     * 单例方法
     * */
    public synchronized static AppConfiguration getInstance(){
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * 加载配置
     * @exception Exception
     * */
    private static AppConfiguration load() {
        FileInputStream fi = null;
        try {
            String path = System.getProperty("user.dir") + File.separator + "conf"+ File.separator + "conf.yml";
            fi = new FileInputStream(new File(path).getAbsolutePath());
            return new Yaml().loadAs(fi, AppConfiguration.class);
        } catch (Exception ex) {
            LogUtil.error("加载配置文件出错！", ex);
            return null;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
            } catch (Exception ex) {
                LogUtil.error("关闭文件流出错！", ex);
            } finally {
                fi = null;
            }
        }
    }

}

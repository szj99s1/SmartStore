package com.daofu.util;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesLoader {
	private static Logger logger = Logger.getLogger(PropertiesLoader.class);
    private static Map<String, PropertiesConfiguration> propertiesConfigurationCache = new ConcurrentHashMap<String, PropertiesConfiguration>();
    private static Map<String, XMLConfiguration> XmlConfigurationCache = new ConcurrentHashMap<String, XMLConfiguration>();

    static {
        AbstractConfiguration.setDefaultListDelimiter('|');
    }

    public synchronized static PropertiesConfiguration getConfiguration(
            String configFileName) {
        if (propertiesConfigurationCache.containsKey(configFileName)) {
            return propertiesConfigurationCache.get(configFileName);
        } else {
            try {
                PropertiesConfiguration config = new PropertiesConfiguration(
                        configFileName + ".properties");
                config.setReloadingStrategy(new FileChangedReloadingStrategy());
                propertiesConfigurationCache.put(configFileName, config);
                return config;
            } catch (ConfigurationException e) {
                logger.error(
                        " configFileName="
                                + configFileName, e);
            }
            return null;
        }
    }

    public synchronized static XMLConfiguration getXmlConfiguration(
            String configFileName) {
        if (XmlConfigurationCache.containsKey(configFileName)) {
            return XmlConfigurationCache.get(configFileName);
        } else {
            try {
                XMLConfiguration config = new XMLConfiguration(configFileName
                        + ".xml");
                config.setReloadingStrategy(new FileChangedReloadingStrategy());
                XmlConfigurationCache.put(configFileName, config);
                return config;
            } catch (ConfigurationException e) {
                System.out.println(e.getMessage());
                logger.error(
                        "getConfiguration(String) - ��ȡ�����ļ��������� - configFileName="
                                + configFileName, e);
            }
            return null;
        }
    }

}

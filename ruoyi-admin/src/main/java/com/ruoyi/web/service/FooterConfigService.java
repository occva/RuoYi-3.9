package com.ruoyi.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.web.domain.FooterConfig;

/**
 * 页脚配置服务
 */
@Service
public class FooterConfigService {

    private static final String FOOTER_CONFIG_PREFIX = "sys.site.footer.";

    private static final String FOOTER_CONFIG_REMARK = "统一维护用户端页脚的品牌文案、联系方式与跳转链接";

    private final ISysConfigService configService;

    public FooterConfigService(ISysConfigService configService) {
        this.configService = configService;
    }

    public FooterConfig getFooterConfig() {
        FooterConfig footerConfig = FooterConfig.defaultConfig();
        Map<String, String> values = getFooterConfigValueMap();

        footerConfig.setBrandName(getString(values, key("brandName"), footerConfig.getBrandName()));
        footerConfig.setBrandTag(getString(values, key("brandTag"), footerConfig.getBrandTag()));
        footerConfig.setDescription(getString(values, key("description"), footerConfig.getDescription()));
        footerConfig.setWechatQrUrl(getString(values, key("wechatQrUrl"), footerConfig.getWechatQrUrl()));
        footerConfig.setQqQrUrl(getString(values, key("qqQrUrl"), footerConfig.getQqQrUrl()));
        footerConfig.setEmail(getString(values, key("email"), footerConfig.getEmail()));
        footerConfig.setCopyrightText(getString(values, key("copyrightText"), footerConfig.getCopyrightText()));

        for (int groupIndex = 0; groupIndex < footerConfig.getGroups().size(); groupIndex++) {
            FooterConfig.FooterLinkGroup group = footerConfig.getGroups().get(groupIndex);
            int groupNumber = groupIndex + 1;
            group.setTitle(getString(values, key("group." + groupNumber + ".title"), group.getTitle()));

            for (int linkIndex = 0; linkIndex < group.getLinks().size(); linkIndex++) {
                FooterConfig.FooterLink link = group.getLinks().get(linkIndex);
                int linkNumber = linkIndex + 1;
                String linkPrefix = "group." + groupNumber + ".link." + linkNumber;
                link.setLabel(getString(values, key(linkPrefix + ".label"), link.getLabel()));
                link.setUrl(getString(values, key(linkPrefix + ".url"), link.getUrl()));
                link.setNewTab(getBoolean(values, key(linkPrefix + ".newTab"), Boolean.TRUE.equals(link.getNewTab())));
            }
        }

        return FooterConfig.normalize(footerConfig);
    }

    public FooterConfig saveFooterConfig(FooterConfig footerConfig, String operator) {
        FooterConfig normalized = FooterConfig.normalize(footerConfig);
        Map<String, SysConfig> records = getFooterConfigRecordMap();

        saveConfig(records, key("brandName"), "页脚配置-品牌名称", normalized.getBrandName(), operator);
        saveConfig(records, key("brandTag"), "页脚配置-英文标识", normalized.getBrandTag(), operator);
        saveConfig(records, key("description"), "页脚配置-简介文案", normalized.getDescription(), operator);
        saveConfig(records, key("wechatQrUrl"), "页脚配置-微信二维码", normalized.getWechatQrUrl(), operator);
        saveConfig(records, key("qqQrUrl"), "页脚配置-QQ二维码", normalized.getQqQrUrl(), operator);
        saveConfig(records, key("email"), "页脚配置-联系邮箱", normalized.getEmail(), operator);
        saveConfig(records, key("copyrightText"), "页脚配置-版权文案", normalized.getCopyrightText(), operator);

        for (int groupIndex = 0; groupIndex < normalized.getGroups().size(); groupIndex++) {
            FooterConfig.FooterLinkGroup group = normalized.getGroups().get(groupIndex);
            int groupNumber = groupIndex + 1;
            saveConfig(records, key("group." + groupNumber + ".title"),
                    "页脚配置-分组" + groupNumber + "-标题", group.getTitle(), operator);

            for (int linkIndex = 0; linkIndex < group.getLinks().size(); linkIndex++) {
                FooterConfig.FooterLink link = group.getLinks().get(linkIndex);
                int linkNumber = linkIndex + 1;
                String configPrefix = "group." + groupNumber + ".link." + linkNumber;
                saveConfig(records, key(configPrefix + ".label"),
                        "页脚配置-分组" + groupNumber + "-链接" + linkNumber + "-文字", link.getLabel(), operator);
                saveConfig(records, key(configPrefix + ".url"),
                        "页脚配置-分组" + groupNumber + "-链接" + linkNumber + "-地址", link.getUrl(), operator);
                saveConfig(records, key(configPrefix + ".newTab"),
                        "页脚配置-分组" + groupNumber + "-链接" + linkNumber + "-新窗",
                        Boolean.TRUE.equals(link.getNewTab()) ? "true" : "false", operator);
            }
        }

        return normalized;
    }

    private void saveConfig(Map<String, SysConfig> records, String configKey, String configName, String configValue,
            String operator) {
        SysConfig record = records.get(configKey);
        String normalizedValue = StringUtils.defaultString(configValue);

        if (StringUtils.isEmpty(normalizedValue)) {
            if (record != null && record.getConfigId() != null) {
                configService.deleteConfigByIds(new Long[] { record.getConfigId() });
            }
            return;
        }

        if (record == null) {
            SysConfig newConfig = new SysConfig();
            newConfig.setConfigName(configName);
            newConfig.setConfigKey(configKey);
            newConfig.setConfigValue(normalizedValue);
            newConfig.setConfigType(UserConstants.YES);
            newConfig.setRemark(FOOTER_CONFIG_REMARK);
            newConfig.setCreateBy(operator);
            configService.insertConfig(newConfig);
            return;
        }

        record.setConfigName(configName);
        record.setConfigValue(normalizedValue);
        record.setConfigType(UserConstants.YES);
        record.setRemark(FOOTER_CONFIG_REMARK);
        record.setUpdateBy(operator);
        configService.updateConfig(record);
    }

    private Map<String, String> getFooterConfigValueMap() {
        Map<String, SysConfig> records = getFooterConfigRecordMap();
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, SysConfig> entry : records.entrySet()) {
            values.put(entry.getKey(), StringUtils.defaultString(entry.getValue().getConfigValue()));
        }
        return values;
    }

    private Map<String, SysConfig> getFooterConfigRecordMap() {
        SysConfig query = new SysConfig();
        query.setConfigKey(FOOTER_CONFIG_PREFIX);
        List<SysConfig> configs = configService.selectConfigList(query);
        Map<String, SysConfig> records = new HashMap<>();
        for (SysConfig config : configs)
        {
            if (StringUtils.startsWith(config.getConfigKey(), FOOTER_CONFIG_PREFIX))
            {
                records.put(config.getConfigKey(), config);
            }
        }
        return records;
    }

    private String getString(Map<String, String> values, String configKey, String defaultValue) {
        return StringUtils.defaultIfBlank(StringUtils.trim(values.get(configKey)), defaultValue);
    }

    private Boolean getBoolean(Map<String, String> values, String configKey, boolean defaultValue) {
        String value = values.get(configKey);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private String key(String suffix) {
        return FOOTER_CONFIG_PREFIX + suffix;
    }
}

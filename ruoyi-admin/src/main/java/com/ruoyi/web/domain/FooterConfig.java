package com.ruoyi.web.domain;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;

/**
 * 用户端页脚配置
 */
public class FooterConfig {

    private String brandName;

    private String brandTag;

    private String description;

    private String wechatQrUrl;

    private String qqQrUrl;

    private String email;

    private List<FooterLinkGroup> groups = new ArrayList<>();

    private String copyrightText;

    public static FooterConfig defaultConfig() {
        FooterConfig config = new FooterConfig();
        config.setBrandName("社团中心");
        config.setBrandTag("Campus Clubs");
        config.setDescription("发现、加入并领导符合你兴趣的社团组织，让大学生活更精彩");
        config.setWechatQrUrl(StringUtils.EMPTY);
        config.setQqQrUrl(StringUtils.EMPTY);
        config.setEmail(StringUtils.EMPTY);
        config.setGroups(defaultGroups());
        config.setCopyrightText("让校园生活更精彩");
        return config;
    }

    public static FooterConfig normalize(FooterConfig source) {
        FooterConfig defaults = defaultConfig();
        if (source == null) {
            return defaults;
        }

        FooterConfig normalized = new FooterConfig();
        normalized.setBrandName(StringUtils.defaultIfBlank(StringUtils.trim(source.getBrandName()), defaults.getBrandName()));
        normalized.setBrandTag(StringUtils.defaultIfBlank(StringUtils.trim(source.getBrandTag()), defaults.getBrandTag()));
        normalized.setDescription(StringUtils.defaultIfBlank(StringUtils.trim(source.getDescription()), defaults.getDescription()));
        normalized.setWechatQrUrl(StringUtils.trim(source.getWechatQrUrl()));
        normalized.setQqQrUrl(StringUtils.trim(source.getQqQrUrl()));
        normalized.setEmail(StringUtils.trim(source.getEmail()));
        normalized.setGroups(normalizeGroups(source.getGroups(), defaults.getGroups()));
        normalized.setCopyrightText(StringUtils.defaultIfBlank(StringUtils.trim(source.getCopyrightText()), defaults.getCopyrightText()));
        return normalized;
    }

    private static List<FooterLinkGroup> defaultGroups() {
        List<FooterLinkGroup> groups = new ArrayList<>();
        groups.add(buildGroup("explore", "探索",
                buildLink("首页", "/user/home", false),
                buildLink("全部社团", "/user/clubs", false),
                buildLink("校园活动", "/user/activities", false)));
        groups.add(buildGroup("mine", "我的",
                buildLink("我的社团", "/user/my-clubs", false),
                buildLink("我的申请", "/user/my-clubs?tab=applications", false),
                buildLink("活动记录", "/user/my-clubs?tab=activities", false)));
        groups.add(buildGroup("help", "帮助",
                buildLink("常见问题", "#", false),
                buildLink("联系客服", "#", false),
                buildLink("意见反馈", "#", false)));
        return groups;
    }

    private static List<FooterLinkGroup> normalizeGroups(List<FooterLinkGroup> sourceGroups, List<FooterLinkGroup> defaultGroups) {
        List<FooterLinkGroup> groups = new ArrayList<>();
        for (int groupIndex = 0; groupIndex < defaultGroups.size(); groupIndex++) {
            FooterLinkGroup defaultGroup = defaultGroups.get(groupIndex);
            FooterLinkGroup sourceGroup = sourceGroups != null && sourceGroups.size() > groupIndex
                    ? sourceGroups.get(groupIndex)
                    : null;
            FooterLinkGroup group = new FooterLinkGroup();
            group.setKey(defaultGroup.getKey());
            group.setTitle(StringUtils.defaultIfBlank(sourceGroup == null ? null : StringUtils.trim(sourceGroup.getTitle()),
                    defaultGroup.getTitle()));
            group.setLinks(normalizeLinks(sourceGroup == null ? null : sourceGroup.getLinks(), defaultGroup.getLinks()));
            groups.add(group);
        }
        return groups;
    }

    private static List<FooterLink> normalizeLinks(List<FooterLink> sourceLinks, List<FooterLink> defaultLinks) {
        List<FooterLink> links = new ArrayList<>();
        for (int linkIndex = 0; linkIndex < defaultLinks.size(); linkIndex++) {
            FooterLink defaultLink = defaultLinks.get(linkIndex);
            FooterLink sourceLink = sourceLinks != null && sourceLinks.size() > linkIndex
                    ? sourceLinks.get(linkIndex)
                    : null;
            FooterLink link = new FooterLink();
            link.setLabel(StringUtils.defaultIfBlank(sourceLink == null ? null : StringUtils.trim(sourceLink.getLabel()),
                    defaultLink.getLabel()));
            link.setUrl(StringUtils.defaultIfBlank(sourceLink == null ? null : StringUtils.trim(sourceLink.getUrl()),
                    defaultLink.getUrl()));
            link.setNewTab(sourceLink == null || sourceLink.getNewTab() == null ? defaultLink.getNewTab()
                    : sourceLink.getNewTab());
            links.add(link);
        }
        return links;
    }

    private static FooterLinkGroup buildGroup(String key, String title, FooterLink... links) {
        FooterLinkGroup group = new FooterLinkGroup();
        group.setKey(key);
        group.setTitle(title);
        List<FooterLink> linkList = new ArrayList<>();
        for (FooterLink link : links) {
            linkList.add(link);
        }
        group.setLinks(linkList);
        return group;
    }

    private static FooterLink buildLink(String label, String url, boolean newTab) {
        FooterLink link = new FooterLink();
        link.setLabel(label);
        link.setUrl(url);
        link.setNewTab(newTab);
        return link;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandTag() {
        return brandTag;
    }

    public void setBrandTag(String brandTag) {
        this.brandTag = brandTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWechatQrUrl() {
        return wechatQrUrl;
    }

    public void setWechatQrUrl(String wechatQrUrl) {
        this.wechatQrUrl = wechatQrUrl;
    }

    public String getQqQrUrl() {
        return qqQrUrl;
    }

    public void setQqQrUrl(String qqQrUrl) {
        this.qqQrUrl = qqQrUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FooterLinkGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<FooterLinkGroup> groups) {
        this.groups = groups;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public static class FooterLinkGroup {

        private String key;

        private String title;

        private List<FooterLink> links = new ArrayList<>();

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<FooterLink> getLinks() {
            return links;
        }

        public void setLinks(List<FooterLink> links) {
            this.links = links;
        }
    }

    public static class FooterLink {

        private String label;

        private String url;

        private Boolean newTab;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Boolean getNewTab() {
            return newTab;
        }

        public void setNewTab(Boolean newTab) {
            this.newTab = newTab;
        }
    }
}

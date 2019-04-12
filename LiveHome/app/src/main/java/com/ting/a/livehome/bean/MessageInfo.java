package com.ting.a.livehome.bean;

/**
 * 新闻，信息实体类
 */
public class MessageInfo {

    private int NewsId;
    private String NewsName;
    private String NewsUrl;
    private String NewsType;
    private int NewsHost;
    private String NewsPicUrl;
    private String NewsCreaDate;
    private String NewsResume;

    public int getNewsId() {
        return NewsId;
    }

    public void setNewsId(int newsId) {
        NewsId = newsId;
    }

    public String getNewsName() {
        return NewsName;
    }

    public void setNewsName(String newsName) {
        NewsName = newsName;
    }

    public String getNewsUrl() {
        return NewsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        NewsUrl = newsUrl;
    }

    public String getNewsType() {
        return NewsType;
    }

    public void setNewsType(String newsType) {
        NewsType = newsType;
    }

    public int getNewsHost() {
        return NewsHost;
    }

    public void setNewsHost(int newsHost) {
        NewsHost = newsHost;
    }

    public String getNewsPicUrl() {
        return NewsPicUrl;
    }

    public void setNewsPicUrl(String newsPicUrl) {
        NewsPicUrl = newsPicUrl;
    }

    public String getNewsCreaDate() {
        return NewsCreaDate;
    }

    public void setNewsCreaDate(String newsCreaDate) {
        NewsCreaDate = newsCreaDate;
    }

    public String getNewsResume() {
        return NewsResume;
    }

    public void setNewsResume(String newsResume) {
        NewsResume = newsResume;
    }
}

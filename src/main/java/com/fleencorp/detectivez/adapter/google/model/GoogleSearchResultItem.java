package com.fleencorp.detectivez.adapter.google.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleSearchResultItem {

  @JsonProperty("kind")
  private String kind;

  @JsonProperty("title")
  private String title;

  @JsonProperty("htmlTitle")
  private String htmlTitle;

  @JsonProperty("link")
  private String link;

  @JsonProperty("displayLink")
  private String displayLink;

  @JsonProperty("snippet")
  private String snippet;

  @JsonProperty("htmlSnippet")
  private String htmlSnippet;

  @JsonProperty("formattedUrl")
  private String formattedUrl;

  @JsonProperty("htmlFormattedUrl")
  private String htmlFormattedUrl;

  @JsonProperty("pagemap")
  private PageMap pageMap;

  @Getter
  @Setter
  public static class PageMap {

    @JsonProperty("cse_thumbnail")
    private List<Thumbnail> cseThumbnail;

    private List<Person> person;

    @JsonProperty("interactioncounter")
    private List<InteractionCounter> interactionCounter;

    private List<Metatag> metatags;

    private List<Collection> collection;

    private List<CreativeWork> creativework;

    @JsonProperty("cse_image")
    private List<Image> cseImage;

    @JsonProperty("socialmediaposting")
    private List<SocialMediaPosting> socialMediaPosting;

    @Getter
    @Setter
    public static class Thumbnail {

      @JsonProperty("src")
      private String src;

      @JsonProperty("width")
      private String width;

      @JsonProperty("height")
      private String height;
    }

    @Getter
    @Setter
    public static class Person {

      @JsonProperty("identifier")
      private String identifier;

      @JsonProperty("givenname")
      private String givenName;

      @JsonProperty("additionalname")
      private String additionalName;
    }

    @Getter
    @Setter
    public static class InteractionCounter {
      @JsonProperty("userinteractioncount")
      private String userInteractionCount;

      @JsonProperty("interactiontype")
      private String interactionType;

      private String name;
      private String url;
    }

    @Getter
    @Setter
    public static class Metatag {

      @JsonProperty("apple-itunes-app")
      private String appleItunesApp;

      @JsonProperty("og:image")
      private String ogImage;

      @JsonProperty("theme-color")
      private String themeColor;

      @JsonProperty("og:type")
      private String ogType;

      @JsonProperty("og:site_name")
      private String ogSiteName;

      @JsonProperty("al:ios:app_name")
      private String alIosAppName;

      @JsonProperty("apple-mobile-web-app-title")
      private String appleMobileWebAppTitle;

      @JsonProperty("og:title")
      private String ogTitle;

      @JsonProperty("al:android:package")
      private String alAndroidPackage;

      @JsonProperty("al:ios:url")
      private String alIosUrl;
      private String title;

      @JsonProperty("og:description")
      private String ogDescription;

      @JsonProperty("al:ios:app_store_id")
      private String alIosAppStoreId;

      @JsonProperty("facebook-domain-verification")
      private String facebookDomainVerification;

      @JsonProperty("al:android:url")
      private String alAndroidUrl;

      @JsonProperty("fb:app_id")
      private String fbAppId;

      @JsonProperty("apple-mobile-web-app-status-bar-style")
      private String appleMobileWebAppStatusBarStyle;
      private String viewport;

      @JsonProperty("mobile-web-app-capable")
      private String mobileWebAppCapable;

      @JsonProperty("og:url")
      private String ogUrl;

      @JsonProperty("al:android:app_name")
      private String alAndroidAppName;
    }

    @Getter
    @Setter
    public static class Collection {

      @JsonProperty("name")
      private String name;
    }

    @Getter
    @Setter
    public static class CreativeWork {

      @JsonProperty("name")
      private String name;

      @JsonProperty("url")
      private String url;
    }

    @Getter
    @Setter
    public static class Image {

      @JsonProperty("src")
      private String src;
    }

    @Getter
    @Setter
    public static class SocialMediaPosting {

      @JsonProperty("identifier")
      private String identifier;

      @JsonProperty("commentcount")
      private String commentCount;

      @JsonProperty("articlebody")
      private String articleBody;

      @JsonProperty("position")
      private String position;

      @JsonProperty("datecreated")
      private String dateCreated;

      @JsonProperty("datepublished")
      private String datePublished;

      @JsonProperty("url")
      private String url;

      @JsonProperty("mainentityofpage")
      private String mainEntityOfPage;

    }
  }
}


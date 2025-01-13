package com.fleencorp.detectivez.service.impl;

import com.fleencorp.detectivez.exception.FailedOperationException;
import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class CrawlerServiceImpl implements CrawlerService {

   /*
    This method
    */
    @Override
    public CrawlerWebsiteResponse investigateTarget(String target) {

        Document result=null;
        try {
            result = Jsoup.connect(target).get();
        } catch (IOException e) {
            throw new FailedOperationException();
        }

        CrawlerWebsiteResponse response = new CrawlerWebsiteResponse();
        if(result!=null){
            response.setTextContent(result.html());
        }
        return response;
    }
}

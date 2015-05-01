package com.weizilla.transit.web.config;

import com.google.common.base.Strings;
import com.weizilla.transit.BusController;
import com.weizilla.transit.source.DataSource;
import com.weizilla.transit.source.stream.InputStreamProvider;
import com.weizilla.transit.source.stream.StreamingDataSource;
import com.weizilla.transit.source.stream.http.HttpInputStreamProvider;
import com.weizilla.transit.source.stream.http.HttpReader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BusControllerFactory
{
    @Resource
    private String ctaApiKey;

    public BusController create()
    {
        checkApiKey();
        HttpReader httpReader = new HttpReader(new HttpReader.HttpURLConnectionFactory());
        InputStreamProvider inputStreamProvider = new HttpInputStreamProvider(httpReader, ctaApiKey);
        DataSource dataSource = new StreamingDataSource(inputStreamProvider);
        return new BusController(dataSource, null, null, null);
    }

    private void checkApiKey()
    {
        if (Strings.isNullOrEmpty(ctaApiKey))
        {
            throw new NoCtaApiKeyException();
        }
    }

    protected void setCtaApiKey(String ctaApiKey)
    {
        this.ctaApiKey = ctaApiKey;
    }
}

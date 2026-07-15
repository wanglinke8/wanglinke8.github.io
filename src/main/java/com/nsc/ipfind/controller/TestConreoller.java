package com.nsc.ipfind.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import nuonuo.open.sdk.NNOpenSDK;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/test/")
public class TestConreoller {


    private NNOpenSDK sdk = NNOpenSDK.getIntance();

    @GetMapping("/redInvoice")
    public String redInvoice() {
        String appKey = "45301931";
        String appSecret = "35DAABEC63164418";
        String effectiveToken = "4499815add85a077a2010f0pzqvmu0is";
        String salerTaxNum = "91445321MA4UXUFRXP";

        String method = "nuonuo.OpeMplatform.saveInvoiceRedConfirm";
        String content = "{\n" +
                "    \"applySource\": \"0\",\n" +
                "    \"blueInvoiceCode\": \"\",\n" +
                "    \"blueInvoiceLine\": \"pc\",\n" +
                "    \"blueInvoiceNumber\": \"26442000003266108941\",\n" +
                "    \"buyerName\": \"个人\",\n" +
                "    \"buyerTaxNo\": \"\",\n" +
                "    \"detail\": [\n" +
                "        {\n" +
                "            \"blueDetailIndex\": \"1\",\n" +
                "            \"goodsName\": \"测试单2勿审核\",\n" +
                "            \"num\": \"-1.0000\",\n" +
                "            \"price\": \"0.01\",\n" +
                "            \"specType\": \"测试单\",\n" +
                "            \"taxAmount\": \"0.00\",\n" +
                "            \"taxExcludedAmount\": \"-0.01\",\n" +
                "            \"taxExcludedPrice\": \"0.0088495575221\",\n" +
                "            \"taxRate\": \"0.13\",\n" +
                "            \"unit\": \"个\",\n" +
                "            \"withTaxFlag\": 1,\n" +
                "            \"goodsCode\": \"1080105030500000000\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"redReason\": \"1\",\n" +
                "    \"sellerTaxNo\": \"91445321MA4UXUFRXP\"\n" +
                "}";
        String url = "https://sdk.nuonuo.com/open/v1/services";
        String senid = UUID.randomUUID().toString().replace("-", "");

        log.info("准备调用诺诺红字确认单申请接口，method={}, url={} ,content={}", method, url, content);
        String resultStr = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, effectiveToken, salerTaxNum, method, content);
        return resultStr;
    }
}

package com.nsc.ipfind.controller;

import com.nsc.ipfind.request.PetQueryRequestDto;
import com.nsc.ipfind.response.PetDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rocowlk/pet")
public class RocoPetController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://wegame.shallow.ink/api/v1/games/rocom/wiki/pets";
    private static final String API_KEY = "sk-6970032caafef05d2b02ff0b99a4a0fd";

    @GetMapping("/getAllPet")
    public List<PetDataDto.PetInfo> getAllPet(PetQueryRequestDto requestDto) {
        log.info("开始查询宠物列表，参数: q={}, type={}, eggGroup={}, skill={}, skillId={}, pageNo={}, pageSize={}",
                requestDto.getQ(), requestDto.getType(), requestDto.getEggGroup(),
                requestDto.getSkill(), requestDto.getSkillId(), requestDto.getPageNo(), requestDto.getPageSize());

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 构建URL和查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("page_no", requestDto.getPageNo())
                .queryParam("page_size", requestDto.getPageSize());

        if (requestDto.getQ() != null && !requestDto.getQ().isEmpty()) {
            builder.queryParam("q", requestDto.getQ());
        }
        if (requestDto.getType() != null && !requestDto.getType().isEmpty()) {
            builder.queryParam("type", requestDto.getType());
        }
        if (requestDto.getEggGroup() != null && !requestDto.getEggGroup().isEmpty()) {
            builder.queryParam("egg_group", requestDto.getEggGroup());
        }
        if (requestDto.getSkill() != null && !requestDto.getSkill().isEmpty()) {
            builder.queryParam("skill", requestDto.getSkill());
        }
        if (requestDto.getSkillId() != null) {
            builder.queryParam("skill_id", requestDto.getSkillId());
        }

        String url = builder.build().toUriString();
        log.info("调用外部API，URL: {}", url);

        // 发送GET请求
        ResponseEntity<PetDataDto.ApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PetDataDto.ApiResponse.class
        );

        PetDataDto.ApiResponse apiResponse = response.getBody();
        if (apiResponse != null && apiResponse.getCode() == 0) {
            PetDataDto.ResponseData data = apiResponse.getData();
            log.info("API调用成功，返回数据: total={}, pageNo={}, pageSize={}, totalPages={}, items数量={}",
                    data.getTotal(), data.getPageNo(), data.getPageSize(),
                    data.getTotalPages(), data.getItems() != null ? data.getItems().size() : 0);


            List<PetDataDto.PetInfo> items = data.getItems();
            log.info(items.toString());

            return items;
        } else {
            log.error("API调用失败，响应码: {}, 消息: {}",
                    apiResponse != null ? apiResponse.getCode() : "null",
                    apiResponse != null ? apiResponse.getMessage() : "未知错误");
            return null;
        }
    }
}

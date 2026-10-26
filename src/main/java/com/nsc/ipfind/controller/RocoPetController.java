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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rocowlk/pet")
public class RocoPetController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private com.nsc.ipfind.service.PetService petService;

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

            // 转换为Pet实体
            List<com.nsc.ipfind.pojos.Pet> allPets = new ArrayList<>();
            List<Integer> petIds = new ArrayList<>();
            for (PetDataDto.PetInfo item : items) {
                petIds.add(item.getPetId());
                com.nsc.ipfind.pojos.Pet pet = new com.nsc.ipfind.pojos.Pet();
                pet.setPetId(item.getPetId());
                pet.setName(item.getName());
                pet.setHandbookNo(item.getHandbookNo());
                pet.setQuality(item.getQuality());
                pet.setStage(item.getStage());
                pet.setSeasonId(item.getSeasonId());
                pet.setHasRideTalent(Boolean.TRUE.equals(item.getHasRideTalent()) ? 1 : 0);
                pet.setHasShiny(Boolean.TRUE.equals(item.getHasShiny()) ? 1 : 0);

                // 设置体型信息
                if (item.getBodySize() != null) {
                    if (item.getBodySize().getHeight() != null) {
                        pet.setHeightMinCm(item.getBodySize().getHeight().getMinCm() != null
                                ? new java.math.BigDecimal(item.getBodySize().getHeight().getMinCm()) : null);
                        pet.setHeightMaxCm(item.getBodySize().getHeight().getMaxCm() != null
                                ? new java.math.BigDecimal(item.getBodySize().getHeight().getMaxCm()) : null);
                    }
                    if (item.getBodySize().getWeight() != null) {
                        pet.setWeightMinG(item.getBodySize().getWeight().getMinG());
                        pet.setWeightMaxG(item.getBodySize().getWeight().getMaxG());
                    }
                }

                pet.setIcon(item.getIcon());
                pet.setSmallIcon(item.getSmallIcon());
                pet.setUpdatedAt(new Date());
                allPets.add(pet);
            }

            // 收集所有handbook_no
            List<String> handbookNos = new ArrayList<>();
            for (com.nsc.ipfind.pojos.Pet pet : allPets) {
                handbookNos.add(pet.getHandbookNo());
            }

            // 查询数据库中已存在的宠物（通过pet_id和handbook_no）
            List<com.nsc.ipfind.pojos.Pet> existingPetsById = petService.listByIds(petIds);
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.nsc.ipfind.pojos.Pet> handbookQuery = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            handbookQuery.in("handbook_no", handbookNos);
            List<com.nsc.ipfind.pojos.Pet> existingPetsByHandbook = petService.list(handbookQuery);

            // 构建已存在宠物的映射：pet_id -> Pet 和 handbook_no -> Pet
            java.util.Map<Integer, com.nsc.ipfind.pojos.Pet> existingPetIdMap = new java.util.HashMap<>();
            java.util.Map<String, com.nsc.ipfind.pojos.Pet> existingHandbookMap = new java.util.HashMap<>();
            for (com.nsc.ipfind.pojos.Pet existingPet : existingPetsById) {
                existingPetIdMap.put(existingPet.getPetId(), existingPet);
            }
            for (com.nsc.ipfind.pojos.Pet existingPet : existingPetsByHandbook) {
                existingHandbookMap.put(existingPet.getHandbookNo(), existingPet);
            }

            // 分离需要更新和插入的数据
            List<com.nsc.ipfind.pojos.Pet> toInsert = new ArrayList<>();
            List<com.nsc.ipfind.pojos.Pet> toUpdate = new ArrayList<>();
            log.info("已存在宠物数量 - 按pet_id查询: {}, 按handbook_no查询: {}", existingPetIdMap.size(), existingHandbookMap.size());
            for (com.nsc.ipfind.pojos.Pet pet : allPets) {
                // 检查pet_id或handbook_no是否已存在
                if (existingPetIdMap.containsKey(pet.getPetId())) {
                    // 通过pet_id找到，直接更新
                    log.debug("宠物 {} (pet_id={}) 通过pet_id匹配，将更新", pet.getName(), pet.getPetId());
                    toUpdate.add(pet);
                } else if (existingHandbookMap.containsKey(pet.getHandbookNo())) {
                    // 通过handbook_no找到，使用原记录的pet_id进行更新
                    com.nsc.ipfind.pojos.Pet existingPet = existingHandbookMap.get(pet.getHandbookNo());
                    log.debug("宠物 {} (handbook_no={}) 通过handbook_no匹配，原pet_id={}，将更新", pet.getName(), pet.getHandbookNo(), existingPet.getPetId());
                    pet.setPetId(existingPet.getPetId());
                    toUpdate.add(pet);
                } else {
                    log.debug("宠物 {} (pet_id={}, handbook_no={}) 不存在，将插入", pet.getName(), pet.getPetId(), pet.getHandbookNo());
                    pet.setCreatedAt(new Date());
                    toInsert.add(pet);
                }
            }
            log.info("待更新宠物数量: {}, 待插入宠物数量: {}", toUpdate.size(), toInsert.size());

            // 分别执行更新和插入
            if (!toUpdate.isEmpty()) {
                petService.updateBatchById(toUpdate);
                log.info("成功更新{}条宠物数据", toUpdate.size());
            }
            if (!toInsert.isEmpty()) {
                petService.saveBatch(toInsert);
                log.info("成功插入{}条宠物数据", toInsert.size());
            }

            return items;
        } else {
            log.error("API调用失败，响应码: {}, 消息: {}",
                    apiResponse != null ? apiResponse.getCode() : "null",
                    apiResponse != null ? apiResponse.getMessage() : "未知错误");
            return null;
        }
    }
}

package com.nsc.ipfind.response;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 宠物数据传输对象 - 接收洛克王国API返回的宠物信息
 */
@Data
public class PetDataDto {

    @Data
    public static class ApiResponse {
        private Integer code;
        private String message;
        private ResponseData data;
    }

    @Data
    public static class ResponseData {
        @JsonProperty("has_more")
        private Boolean hasMore;

        private List<PetInfo> items;

        @JsonProperty("page_no")
        private Integer pageNo;

        @JsonProperty("page_size")
        private Integer pageSize;

        private Integer total;

        @JsonProperty("total_pages")
        private Integer totalPages;
    }

    @Data
    public static class PetInfo {
        @JsonProperty("body_size")
        private BodySize bodySize;
        
        @JsonProperty("egg_group_ids")
        private List<Integer> eggGroupIds;
        
        @JsonProperty("egg_group_names")
        private List<String> eggGroupNames;
        
        @JsonProperty("handbook_no")
        private String handbookNo;
        
        @JsonProperty("has_ride_talent")
        private Boolean hasRideTalent;
        
        @JsonProperty("has_shiny")
        private Boolean hasShiny;
        
        private String icon;
        
        private String name;
        
        @JsonProperty("pet_id")
        private Integer petId;
        
        private Integer quality;
        
        @JsonProperty("season_id")
        private Integer seasonId;
        
        @JsonProperty("small_icon")
        private String smallIcon;
        
        private Integer stage;
        
        @JsonProperty("type_ids")
        private List<Integer> typeIds;
        
        @JsonProperty("type_names")
        private List<String> typeNames;
    }

    @Data
    public static class BodySize {
        private Height height;
        private Weight weight;
    }

    @Data
    public static class Height {
        @JsonProperty("max_cm")
        private Integer maxCm;
        
        @JsonProperty("max_m")
        private Double maxM;
        
        @JsonProperty("min_cm")
        private Integer minCm;
        
        @JsonProperty("min_m")
        private Double minM;
    }

    @Data
    public static class Weight {
        @JsonProperty("max_g")
        private Integer maxG;
        
        @JsonProperty("max_kg")
        private Double maxKg;
        
        @JsonProperty("min_g")
        private Integer minG;
        
        @JsonProperty("min_kg")
        private Double minKg;
    }
}

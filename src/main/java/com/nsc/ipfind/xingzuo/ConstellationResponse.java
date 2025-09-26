package com.nsc.ipfind.xingzuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// 星座运势响应类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstellationResponse {
    private Integer code;
    private String msg;
    private Map<String, ConstellationData> data;
}

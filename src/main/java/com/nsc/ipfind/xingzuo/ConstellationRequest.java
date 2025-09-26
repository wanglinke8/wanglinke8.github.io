package com.nsc.ipfind.xingzuo;

import lombok.Data;

@Data
public class ConstellationRequest {
    private String constellation; // 星座名称，如：Aries, Taurus
    private String type; // 查询类型：today, tomorrow, weeks, month, years
}

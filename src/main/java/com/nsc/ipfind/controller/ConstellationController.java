package com.nsc.ipfind.controller;

import com.nsc.ipfind.service.ConstellationService;
import com.nsc.ipfind.xingzuo.ConstellationRequest;
import com.nsc.ipfind.xingzuo.ConstellationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/constellation")
@Slf4j
public class ConstellationController {

    @Autowired
    private ConstellationService constellationService; // 注入接口，Spring会自动注入实现类

    @GetMapping
    public ResponseEntity<Map<String, Object>> getConstellationInfo(
            @RequestParam String constellation) {
        try {
            ConstellationResponse response = constellationService.getConstellationInfo(constellation);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", response.getData().get(constellation));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询星座信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("msg", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<Map<String, Object>> getConstellationDetail(
            @RequestParam String constellation,
            @RequestParam(defaultValue = "today") String type) {
        try {
            Object detail = constellationService.getSpecificConstellationInfo(constellation, type);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", detail);
            result.put("type", type);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询星座详细信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("msg", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> queryConstellation(@RequestBody ConstellationRequest request) {
        try {
            Object result = constellationService.getSpecificConstellationInfo(
                    request.getConstellation(),
                    request.getType()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "success");
            response.put("data", result);
            response.put("constellation", request.getConstellation());
            response.put("type", request.getType());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询星座信息失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("msg", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}

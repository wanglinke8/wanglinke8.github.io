package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.pojos.LocationResults;
import com.nsc.ipfind.service.LocationResultsService;
import com.nsc.ipfind.mapper.LocationResultsMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【location_results(地理位置查询结果表)】的数据库操作Service实现
* @createDate 2025-07-23 11:10:44
*/
@Service
public class LocationResultsServiceImpl extends ServiceImpl<LocationResultsMapper, LocationResults>
    implements LocationResultsService{

}





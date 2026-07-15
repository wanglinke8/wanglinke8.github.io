package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.mapper.PetTypeMapper;
import com.nsc.ipfind.pojos.PetType;

import com.nsc.ipfind.service.PetTypeService;
import org.springframework.stereotype.Service;

/**
* @author 28380
* @description 针对表【pet_type(宠物属性/系别字典表)】的数据库操作Service实现
* @createDate 2026-07-15 17:47:57
*/
@Service
public class PetTypeServiceImpl extends ServiceImpl<PetTypeMapper, PetType>
    implements PetTypeService {

}





package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.mapper.PetTypeRelMapper;
import com.nsc.ipfind.pojos.PetTypeRel;

import com.nsc.ipfind.service.PetTypeRelService;
import org.springframework.stereotype.Service;

/**
* @author 28380
* @description 针对表【pet_type_rel(宠物-属性多对多关联表(无外键))】的数据库操作Service实现
* @createDate 2026-07-15 17:47:57
*/
@Service
public class PetTypeRelServiceImpl extends ServiceImpl<PetTypeRelMapper, PetTypeRel>
    implements PetTypeRelService {

}





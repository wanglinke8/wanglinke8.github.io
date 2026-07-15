package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.mapper.PetEggGroupRelMapper;
import com.nsc.ipfind.pojos.PetEggGroupRel;

import com.nsc.ipfind.service.PetEggGroupRelService;
import org.springframework.stereotype.Service;

/**
* @author 28380
* @description 针对表【pet_egg_group_rel(宠物-蛋组多对多关联表(无外键))】的数据库操作Service实现
* @createDate 2026-07-15 17:47:57
*/
@Service
public class PetEggGroupRelServiceImpl extends ServiceImpl<PetEggGroupRelMapper, PetEggGroupRel>
    implements PetEggGroupRelService {

}





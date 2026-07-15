package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.mapper.PetMapper;
import com.nsc.ipfind.pojos.Pet;

import com.nsc.ipfind.service.PetService;
import org.springframework.stereotype.Service;

/**
* @author 28380
* @description 针对表【pet(宠物主表)】的数据库操作Service实现
* @createDate 2026-07-15 17:47:57
*/
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet>
    implements PetService {

}





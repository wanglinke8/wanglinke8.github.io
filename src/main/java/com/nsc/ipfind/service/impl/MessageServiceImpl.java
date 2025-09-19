package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.pojos.Message;
import com.nsc.ipfind.service.MessageService;
import com.nsc.ipfind.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【message】的数据库操作Service实现
* @createDate 2025-09-19 16:34:33
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}





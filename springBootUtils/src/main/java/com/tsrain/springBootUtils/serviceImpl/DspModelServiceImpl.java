package com.tsrain.springBootUtils.serviceImpl;

import com.tsrain.springBootUtils.entity.DspModel;
import com.tsrain.springBootUtils.mapper.DspModelMapper;
import com.tsrain.springBootUtils.service.IDspModelService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cuitianyu
 * @since 2018-12-19
 */
@Service
public class DspModelServiceImpl extends ServiceImpl<DspModelMapper, DspModel> implements IDspModelService {
	
}

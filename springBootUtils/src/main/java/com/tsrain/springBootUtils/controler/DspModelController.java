package com.tsrain.springBootUtils.controler;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tsrain.springBootUtils.entity.DspModel;
import com.tsrain.springBootUtils.service.IDspModelService;
import com.tsrain.springBootUtils.serviceImpl.DspModelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cuitianyu
 * @since 2018-12-19
 */
@Controller
@RequestMapping("/dspModel")
public class DspModelController {
    @Autowired
    private IDspModelService iDspModelService;

    public List getDbinfo(){
        EntityWrapper ew=new EntityWrapper();
        DspModel dsp = new DspModel();
        dsp.setStatus("1");
        ew.setEntity(dsp);
        Page page = new Page(1,10);
        page = iDspModelService.selectPage(page,ew);
        return page.getRecords();
    }
	
}

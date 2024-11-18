package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.southwind.entity.InOutRecord;
import com.southwind.form.InOutQueryForm;
import com.southwind.mapper.InOutRecordMapper;
import com.southwind.mapper.ParkMapper;
import com.southwind.service.InOutRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.vo.ChartVO;
import com.southwind.vo.InOutRecordVO;
import com.southwind.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-07-26
 */
@Service
public class InOutRecordServiceImpl extends ServiceImpl<InOutRecordMapper, InOutRecord> implements InOutRecordService {

    @Autowired
    private InOutRecordMapper inOutRecordMapper;
    @Autowired
    private ParkMapper parkMapper;

    /**
     * 获取停车场收支图表数据
     *
     * 此方法从数据库中获取收支数据，将其整理成图表需要的格式
     * 它提取了每个记录的名称和值，分别存储在两个列表中，然后将这两个列表放入一个映射中返回
     *
     * @return 包含图表数据的映射，包括名称列表和数值列表
     */
    @Override
    public Map chart() {
        List<ChartVO> chartVOList = this.inOutRecordMapper.chart();
        List<String> names = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        for (ChartVO chartVO : chartVOList) {
            names.add(chartVO.getName());
            nums.add(chartVO.getValue());
        }
        Map<String,List> map = new HashMap<>();
        map.put("names", names);
        map.put("nums", nums);
        return map;
    }

    /**
     * 查询停车场收支记录列表
     *
     * 此方法根据查询条件分页查询收支记录，并将结果转换为自定义的PageVO对象返回
     * 它首先创建一个分页对象和一个查询条件对象，然后根据查询条件查询数据库
     * 查询条件包括车牌号、开始日期和结束日期查询结果被转换为一个列表，其中每个记录都被封装成一个InOutRecordVO对象
     *
     * @param inOutQueryForm 查询表单，包含查询条件和分页信息
     * @return 包含收支记录列表和分页信息的PageVO对象
     */
    @Override
    public PageVO inOutRecordList(InOutQueryForm inOutQueryForm) {
        Page<InOutRecord> page = new Page<>(inOutQueryForm.getPage(),inOutQueryForm.getLimit());
        QueryWrapper<InOutRecord> inOutRecordQueryWrapper = new QueryWrapper<>();
        inOutRecordQueryWrapper.like(StringUtils.isNotBlank(inOutQueryForm.getNumber()), "number", inOutQueryForm.getNumber());
        inOutRecordQueryWrapper.between(StringUtils.isNotBlank(inOutQueryForm.getStartDate()) && StringUtils.isNotBlank(inOutQueryForm.getEndDate()), "in_time", inOutQueryForm.getStartDate(), inOutQueryForm.getEndDate());
        Page<InOutRecord> resultPage = this.inOutRecordMapper.selectPage(page, inOutRecordQueryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setTotalCount(resultPage.getTotal());
        pageVO.setPageSize(resultPage.getSize());
        pageVO.setCurrPage(resultPage.getCurrent());
        pageVO.setTotalPage(resultPage.getPages());
        List<InOutRecordVO> list = new ArrayList<>();
        for (InOutRecord record : resultPage.getRecords()) {
            InOutRecordVO inOutRecordVO = new InOutRecordVO();
            BeanUtils.copyProperties(record, inOutRecordVO);
            if(record.getPayType() == 1) {
                inOutRecordVO.setPayType("临时车");
            } else {
                inOutRecordVO.setPayType("包月车");
            }
            inOutRecordVO.setParkName(this.parkMapper.selectById(record.getParkId()).getParkName());
            list.add(inOutRecordVO);
        }
        pageVO.setList(list);
        return pageVO;
    }
}

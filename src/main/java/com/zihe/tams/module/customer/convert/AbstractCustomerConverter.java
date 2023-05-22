package com.zihe.tams.module.customer.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zihe.tams.module.customer.model.data.CustomerDO;
import com.zihe.tams.module.customer.model.dto.CustomerSaveDTO;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/10 10:50 下午
 */
@Mapper
public abstract class AbstractCustomerConverter {
    public static AbstractCustomerConverter INSTANCE = Mappers.getMapper(AbstractCustomerConverter.class);

    public abstract Page<CustomerListVO> doPage2ListVoPage(IPage<CustomerDO> doPage);

    public abstract List<CustomerListVO> doList2ListVoList(List<CustomerDO> doList);

    public abstract CustomerListVO do2ListVO(CustomerDO dataObj);

    public abstract CustomerDO saveDto2DO(CustomerSaveDTO saveDTO);

}

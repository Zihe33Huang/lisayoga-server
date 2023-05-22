package com.zihe.tams.common.util;

import com.zihe.tams.module.coursescheduling.dao.CourseSchedulingMapper;
import com.zihe.tams.module.customer.dao.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangzihe
 * @date 2022/3/15 9:38 下午
 */
@Component
public class ExistCheckUtils {
    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CourseSchedulingMapper courseSchedulingMapper;

    public boolean isCustomerExist(Long customerId) {
        return customerMapper.selectById(customerId) != null ? true : false;
    }

    public boolean isScheduleExist(Long scheduleId) {
        return courseSchedulingMapper.selectById(scheduleId) != null ? true : false;
    }
}

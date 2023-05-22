package com.zihe.tams.module.teacher.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/9 8:58 下午
 */
@Data
public class TeacherConfigQuery extends BasePageQuery {
    Long teacherId;

    String name;
}

package com.hb.shardingjdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hb.shardingjdbc.model.Student;

/**
* 作者： duay
* 时间：2022/06/09
* 描述：继承mybatisplus提供的BaseMapper，这样就不用写crud了
*/
public interface StudentMapper extends BaseMapper<Student> {
    
}

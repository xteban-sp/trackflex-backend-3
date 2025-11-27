package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.CanalDTO;
import com.rutaflex.trackflexv3.entity.Canal;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CanalMapper extends BaseMapper<Canal, CanalDTO> {

}
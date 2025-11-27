package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.ControlDTO;
import com.rutaflex.trackflexv3.entity.Control;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ControlMapper extends BaseMapper<Control, ControlDTO> {
}
package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.DistritoDTO;
import com.rutaflex.trackflexv3.entity.Distrito;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistritoMapper extends BaseMapper<Distrito, DistritoDTO> {
}
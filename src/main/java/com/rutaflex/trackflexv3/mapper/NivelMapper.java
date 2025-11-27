package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.NivelDTO;
import com.rutaflex.trackflexv3.entity.Nivel;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NivelMapper extends BaseMapper<Nivel, NivelDTO> {
}
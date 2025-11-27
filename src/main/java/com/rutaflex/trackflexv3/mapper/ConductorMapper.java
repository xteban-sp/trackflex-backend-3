package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.ConductorDTO;
import com.rutaflex.trackflexv3.entity.Conductor;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConductorMapper extends BaseMapper<Conductor, ConductorDTO> {
}
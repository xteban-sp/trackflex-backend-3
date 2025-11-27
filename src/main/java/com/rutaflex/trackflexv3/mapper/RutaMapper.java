package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.RutaDTO;
import com.rutaflex.trackflexv3.entity.Ruta;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RutaMapper extends BaseMapper<Ruta, RutaDTO> {
}
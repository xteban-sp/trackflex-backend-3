package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TipoDocDTO;
import com.rutaflex.trackflexv3.entity.TipoDoc;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoDocMapper extends BaseMapper<TipoDoc, TipoDocDTO> {
}
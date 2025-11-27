package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TipoGastoDTO;
import com.rutaflex.trackflexv3.entity.TipoGasto;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface
TipoGastoMapper extends BaseMapper<TipoGasto, TipoGastoDTO> {
}

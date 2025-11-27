package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TipoUrgenciaDTO;
import com.rutaflex.trackflexv3.entity.TipoUrgencia;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoUrgenciaMapper extends BaseMapper<TipoUrgencia, TipoUrgenciaDTO> {

}
package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.EstadoDTO;
import com.rutaflex.trackflexv3.entity.Estado;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper extends BaseMapper<Estado, EstadoDTO> {

    @Override
    EstadoDTO toDTO(Estado entity);

    @Override
    Estado toEntity(EstadoDTO dto);
}
package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.AlmacenDTO;
import com.rutaflex.trackflexv3.entity.Almacen;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlmacenMapper extends BaseMapper<Almacen, AlmacenDTO> {
}
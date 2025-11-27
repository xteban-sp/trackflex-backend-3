package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.RolDTO;
import com.rutaflex.trackflexv3.entity.security.Rol;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RolMapper extends BaseMapper<Rol, RolDTO> {
}
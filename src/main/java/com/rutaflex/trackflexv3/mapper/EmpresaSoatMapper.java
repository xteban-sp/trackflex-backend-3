package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.EmpresaSoatDTO;
import com.rutaflex.trackflexv3.entity.EmpresaSoat;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaSoatMapper extends BaseMapper<EmpresaSoat, EmpresaSoatDTO> {

}
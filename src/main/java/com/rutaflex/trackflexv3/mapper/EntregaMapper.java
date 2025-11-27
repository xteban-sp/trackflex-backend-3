package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.EntregaDTO;
import com.rutaflex.trackflexv3.entity.Entrega;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntregaMapper extends BaseMapper<Entrega, EntregaDTO> {

    @Override
    @Mapping(target = "psexId", source = "psex.id")
    @Mapping(target = "canalId", source = "canal.id")
    @Mapping(target = "servicioId", source = "servicio.id")
    @Mapping(target = "tiendaId", source = "tienda.id")
    @Mapping(target = "estadoId", source = "estado.id")
    EntregaDTO toDTO(Entrega entity);

    @Override
    @Mapping(target = "psex", ignore = true)
    @Mapping(target = "canal", ignore = true)
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "tienda", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Entrega toEntity(EntregaDTO dto);
}
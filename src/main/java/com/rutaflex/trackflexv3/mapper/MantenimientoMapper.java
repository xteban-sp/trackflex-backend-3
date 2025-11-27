package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.MantenimientoDTO;
import com.rutaflex.trackflexv3.entity.Mantenimiento;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MantenimientoMapper extends BaseMapper<Mantenimiento, MantenimientoDTO> {

    @Override
    @Mapping(target = "vehiculoId", source = "vehiculo.id")
    @Mapping(target = "tipoMantenimientoId", source = "tipoMantenimiento.id")
    @Mapping(target = "tipoUrgenciaId", source = "tipoUrgencia.id")
    MantenimientoDTO toDTO(Mantenimiento entity);

    @Override
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "tipoMantenimiento", ignore = true)
    @Mapping(target = "tipoUrgencia", ignore = true)
    Mantenimiento toEntity(MantenimientoDTO dto);
}
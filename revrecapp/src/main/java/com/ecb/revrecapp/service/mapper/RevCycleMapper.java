package com.ecb.revrecapp.service.mapper;

import com.ecb.revrecapp.domain.*;
import com.ecb.revrecapp.service.dto.RevCycleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RevCycle and its DTO RevCycleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RevCycleMapper {

    RevCycleDTO revCycleToRevCycleDTO(RevCycle revCycle);

    List<RevCycleDTO> revCyclesToRevCycleDTOs(List<RevCycle> revCycles);

    RevCycle revCycleDTOToRevCycle(RevCycleDTO revCycleDTO);

    List<RevCycle> revCycleDTOsToRevCycles(List<RevCycleDTO> revCycleDTOs);
}

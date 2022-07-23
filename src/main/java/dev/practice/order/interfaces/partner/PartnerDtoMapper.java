package dev.practice.order.interfaces.partner;

import dev.practice.order.domain.partner.PartnerCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PartnerDtoMapper {

    //target 과 method (Source source) 만 지정해주면 됨.
    //Target: PartnerCommand / Source: PartnerDto의 RegisterRequest
    PartnerCommand of(PartnerDto.RegisterRequest request);
}

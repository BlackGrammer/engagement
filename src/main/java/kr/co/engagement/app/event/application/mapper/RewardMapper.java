package kr.co.engagement.app.event.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import kr.co.engagement.app.event.application.dto.RewardIssueDto;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.core.util.BaseMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RewardMapper extends BaseMapper<Reward, RewardIssueDto> {

    @Override
    @Mapping(source = "eventTitle", target = "event.title")
    @Mapping(source = "eventType", target = "event.eventType")
    Reward toEntity(RewardIssueDto dto);

    @Override
    @Mapping(target = "eventTitle", source = "event.title")
    @Mapping(target = "eventType", source = "event.eventType")
    RewardIssueDto toDto(Reward entity);

    @Override
    List<Reward> toEntityList(List<RewardIssueDto> dto);

    @Override
    List<RewardIssueDto> toDtoList(List<Reward> dto);
}

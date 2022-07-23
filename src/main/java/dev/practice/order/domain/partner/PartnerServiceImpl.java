package dev.practice.order.domain.partner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {
    private final PartnerStore partnerStore;
    private final PartnerReader partnerReader;

    @Override
    @Transactional
    public PartnerInfo registerPartner(PartnerCommand command) {
        //Command로 넘어온 값을 initPartner로 변환
        //Partner.builder를 통해 만들어도 됨.
        //그런데 좀 더 단순화하기 위해 Command 내부에서 Entity 생성해줌
        /** initPartner와 var는 뭐지? */
        var initPartner = command.toEntity();
        //initPartner를 DB에 저장
        Partner partner = partnerStore.store(initPartner);
        //Partner를 PartnerInfo 로 변환해서 return
        //마찬가지로 PartnerInfo.builder()를 통해 만들어도 됨.
        //이렇게 PartnerInfo 내부적으로 만들어주는 게 편하긴 함.
        return new PartnerInfo(partner);
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerInfo getPartnerInfo(String partnerToken) {
        // PartnerToken으로 Partner 가져온 후
        Partner partner = partnerReader.getPartner(partnerToken);
        // Partner -> PartnerInfo로 리턴
        return new PartnerInfo(partner);
    }

    @Override
    @Transactional
    public PartnerInfo enablePartner(String partnerToken) {
        Partner partner = partnerReader.getPartner(partnerToken);
        //변경감지 사용한 건가?
        partner.enable();
        return new PartnerInfo(partner);
    }

    @Override
    @Transactional
    public PartnerInfo disablePartner(String partnerToken) {
        Partner partner = partnerReader.getPartner(partnerToken);
        partner.disable();
        return new PartnerInfo(partner);
    }
}

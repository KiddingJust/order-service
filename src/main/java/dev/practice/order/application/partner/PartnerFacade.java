package dev.practice.order.application.partner;

import dev.practice.order.domain.notification.NotificationService;
import dev.practice.order.domain.partner.PartnerCommand;
import dev.practice.order.domain.partner.PartnerInfo;
import dev.practice.order.domain.partner.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerFacade {
    private final PartnerService partnerService;
    private final NotificationService notificationService;

    public PartnerInfo registerPartner(PartnerCommand command) {
        // partnerService에서 register
        var partnerInfo = partnerService.registerPartner(command);
        // 이메일 발송. 이메일 성공 실패 관계없이 파트너 등록은 유지. 따라서 Transaction은 여기에 걸리진 않음.
        notificationService.sendEmail(partnerInfo.getEmail(), "title", "description");
        return partnerInfo;
    }
}

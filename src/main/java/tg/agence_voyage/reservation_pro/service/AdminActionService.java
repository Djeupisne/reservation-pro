package tg.agence_voyage.reservation_pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tg.agence_voyage.reservation_pro.entity.AdminAction;
import tg.agence_voyage.reservation_pro.event.ActionSavedEvent;
import tg.agence_voyage.reservation_pro.repository.AdminActionRepository;

@Service
public class AdminActionService {
    @Autowired
    private AdminActionRepository adminActionRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public AdminAction saveAdminAction(AdminAction adminAction) {
        AdminAction savedAction = adminActionRepository.save(adminAction);
        eventPublisher.publishEvent(new ActionSavedEvent(this));
        return savedAction;
    }

    public Iterable<AdminAction> getAllAdminActions() {
        return adminActionRepository.findAll();
    }
}
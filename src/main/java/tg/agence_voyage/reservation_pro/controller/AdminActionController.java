package tg.agence_voyage.reservation_pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.agence_voyage.reservation_pro.dto.AdminActionDTO;
import tg.agence_voyage.reservation_pro.entity.AdminAction;
import tg.agence_voyage.reservation_pro.service.AdminActionService;

@RestController
@RequestMapping("/test")
public class AdminActionController {

    @Autowired
    private AdminActionService adminActionService;

    @PostMapping("/admin-action")
    public ResponseEntity<AdminAction> saveAdminAction(@RequestBody AdminActionDTO adminActionDTO) {
        AdminAction adminAction = new AdminAction();
        adminAction.setActionType(adminActionDTO.getActionType());
        AdminAction savedAction = adminActionService.saveAdminAction(adminAction);
        return ResponseEntity.ok(savedAction);
    }

    @GetMapping("/admin-actions")
    public ResponseEntity<Iterable<AdminAction>> getAllAdminActions() {
        Iterable<AdminAction> actions = adminActionService.getAllAdminActions();
        return ResponseEntity.ok(actions);
    }
}
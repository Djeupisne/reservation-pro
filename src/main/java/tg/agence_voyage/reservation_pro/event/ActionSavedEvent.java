package tg.agence_voyage.reservation_pro.event;

import org.springframework.context.ApplicationEvent;

public class ActionSavedEvent extends ApplicationEvent {
    private String eventTimestamp;

    public ActionSavedEvent(Object source) {
        super(source);
        this.eventTimestamp = String.valueOf(System.currentTimeMillis());
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
}
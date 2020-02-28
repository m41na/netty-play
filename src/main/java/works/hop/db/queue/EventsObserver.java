package works.hop.db.queue;

import works.hop.db.entity.EventEntity;
import works.hop.db.record.EventEntityRecord;

import java.util.Optional;

public class EventsObserver extends QuObserver {

    private final EventEntityRecord records;

    public EventsObserver() {
        this.records = new EventEntityRecord();
    }

    @Override
    public void receive(EventEntity event) {
            if (EXIT.equals(event)) {
                complete();
            } else {
                Optional<Long> result = records.insert(EventEntityRecord.insertRecord, event, false);
                System.out.println("events inserted : " + result);
            }
    }

    @Override
    public void complete() {

    }
}

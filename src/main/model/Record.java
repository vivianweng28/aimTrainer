package model;

import java.util.ArrayList;
import java.util.List;
import model.Session;

public class Record {
    private List sessions;

    public Record() {
        sessions = new ArrayList<Session>();
    }

    public void addSession(Session s) {
        sessions.add(s);
    }
}

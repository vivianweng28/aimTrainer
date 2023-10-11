package model;

import model.Session;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private List<Session> sessions;

    public Record() {
        sessions = new ArrayList<Session>();
    }

    public void addSession(Session s) {
        sessions.add(s);
    }

    public Session getSession(int index) {
        return sessions.get(index);
    }

    public int getNumSessions () {
        return sessions.size();
    }
}

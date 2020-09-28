package com.example.mynotes;
import java.time.LocalDateTime;

public class Note {

    String note;
    String title;
    String lastSave;

    Note(String note, String title, String lastSave) {
        this.note = note;
        this.title = title;
        this.lastSave = lastSave;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }

    public String getLastSave() {
        return lastSave;
    }

    public void setNote(String text) {
        this.note = text;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public void setLastSave(String time) {
        this.lastSave = time;
    }
}

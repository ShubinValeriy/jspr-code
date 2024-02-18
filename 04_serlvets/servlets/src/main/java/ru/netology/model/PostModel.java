package ru.netology.model;

public class PostModel extends Post{
    private boolean isActive;

    public PostModel(long id, String content, boolean isActive) {
        super(id, content);
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

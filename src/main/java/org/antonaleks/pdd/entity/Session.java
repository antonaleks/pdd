package org.antonaleks.pdd.entity;

import org.antonaleks.pdd.model.Category;

public final class Session {

    public User getCurrentUser() {
        return currentUser;
    }

    private static volatile Session instance = null;

    private User currentUser;

    public Category getCurrentCategory() {
        return currentCategory;
    }

    private Session() {
    }

    private Category currentCategory;

    public void init(User currentUser, Category currentCategory) {

        this.currentUser = currentUser;
        this.currentCategory = currentCategory;
    }

    public static Session getInstance() {
        if (instance == null) {
            synchronized (Session.class) {
                if (instance == null) {
                    instance = new Session();
                }
            }
        }
        return instance;
    }

}

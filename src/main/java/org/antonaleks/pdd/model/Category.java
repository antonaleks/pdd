package org.antonaleks.pdd.model;

public enum Category {

    AB(0),
    CD(1),
    ;

    Category(int i) {
        this.cat = i;
    }

    private int cat;

   public int getCategory() {
       return this.cat;
   }

    @Override
    public String toString() {
        return "Категория " + this.name();
    }
}

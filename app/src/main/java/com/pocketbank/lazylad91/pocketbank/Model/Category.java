package com.pocketbank.lazylad91.pocketbank.Model;

/**
 * Created by Parteek on 7/29/2016.
 */
public class Category {
    public Category(int id, String name, String image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                '}';
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private int id;
    private String image;
}

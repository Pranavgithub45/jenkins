package com.billdesk.forjenkins.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Table(name = "players")
@Entity
public class player {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Id
    private Integer jerseyNo;

    public player(String name, int age, int jerseyNo) {
        this.name = name;
        this.age = age;
        this.jerseyNo = jerseyNo;
    }

    public player() {
    }

    @Override
    public String toString() {
        return "player{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", jerseyNo=" + jerseyNo +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getJerseyNo() {
        return jerseyNo;
    }

    public void setJerseyNo(int jerseyNo) {
        this.jerseyNo = jerseyNo;
    }
}

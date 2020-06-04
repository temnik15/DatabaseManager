package ru.temnik.databaseManager.Entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //Указывает, что наша Аннотация может быть использована во время выполнения через Reflection (нам как раз это нужно).
@Target(ElementType.METHOD)
public @interface Test {
    String tablename();
}

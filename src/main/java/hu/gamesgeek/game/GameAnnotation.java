package hu.gamesgeek.game;

import hu.gamesgeek.types.GameType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GameAnnotation {

    GameType gameType();

    Class dtoClass();

    Class settingsClass() default void.class;
}

package com.learn.snake.model;

import net.bytebuddy.description.modifier.ModifierContributor;

import java.lang.reflect.Type;

/**
 * @Author :lwy
 * @Date : 2018/10/23 18:13
 * @Description :
 */
public class FieldDefine {

    public String name;

    public Type type;

    public ModifierContributor.ForField[] modifiers;


    /**
     *
     * @param name
     * @param type
     * @param modifiers Visibility.PUBLIC, Ownership.STATIC, FieldManifestation.FINAL
     */
    public FieldDefine(String name, Type type, ModifierContributor.ForField[] modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }
}

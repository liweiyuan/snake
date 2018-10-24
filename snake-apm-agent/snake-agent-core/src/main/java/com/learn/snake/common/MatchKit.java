package com.learn.snake.common;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/24 15:06
 * @Description :
 */
public class MatchKit {

    /**
     * 类型匹配： 类型的规则适配于排除
     *
     * @param includeMap
     * @param excludeMap
     * @return
     */
    public static ElementMatcher.Junction buildTypesMatcher(Map<String, String> includeMap, Map<String, String> excludeMap) {

        ElementMatcher.Junction<TypeDescription> matcher = ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("com.learn"));

        if (includeMap != null && !includeMap.isEmpty()) {
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String hasSuperTypeVal = includeMap.get("hasSuperType");
            String hasAnnotationVal = includeMap.get("hasAnnotation");

            ElementMatcher.Junction<TypeDescription> includeMatcher = null;
            if (StringUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.named(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.named(arr[i]));
                }
            }
            if (StringUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameStartsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameStartsWith(arr[i]));
                }
            }
            if (StringUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameEndsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameEndsWith(arr[i]));
                }
            }
            if (StringUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameContains(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameContains(arr[i]));
                }
            }
            if (StringUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameMatches(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameMatches(arr[i]));
                }
            }
            if (StringUtils.isNotBlank(hasSuperTypeVal)) {
                String[] arr = hasSuperTypeVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i]));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(hasAnnotationVal)) {
                String[] arr = hasAnnotationVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i])));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
            if (includeMatcher != null) {
                matcher = matcher.and(includeMatcher);
            }
        }


        //排除
        if (excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String hasSuperTypeVal = excludeMap.get("hasSuperType");
            String hasAnnotationVal = excludeMap.get("hasAnnotation");
            ElementMatcher.Junction<TypeDescription> excludeMatch = ElementMatchers.none();
            if (StringUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameEndsWith(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameContains(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameMatches(arr[i])));
                }
            }
            if (StringUtils.isNotBlank(hasSuperTypeVal)) {
                String[] arr = hasSuperTypeVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
            if (StringUtils.isNotBlank(hasAnnotationVal)) {
                String[] arr = hasAnnotationVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i])))));
                }
            }

        }
        return matcher;
    }

    /**
     * 方法匹配： 类型的规则适配于排除
     * @return
     */
    public static ElementMatcher<MethodDescription> buildMethodsMatcher(Map<String,String> includeMap,Map<String,String> excludeMap) {
        ElementMatcher.Junction<MethodDescription>  matcher = ElementMatchers.isMethod();
        if(includeMap != null && !includeMap.isEmpty()) {
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String isAnnotatedWith = includeMap.get("isAnnotatedWith");
            ElementMatcher.Junction<MethodDescription>  includeMatcher = null;
            if(StringUtils.isNotBlank(namedVal)){
                String[] arr = namedVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.named(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.named(arr[i]));
                }
            }
            if(StringUtils.isNotBlank(nameStartsWithVal)){
                String[] arr = nameStartsWithVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.nameStartsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameStartsWith(arr[i]));
                }
            }
            if(StringUtils.isNotBlank(nameEndsWithVal)){
                String[] arr = nameEndsWithVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.nameEndsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameEndsWith(arr[i]));
                }
            }
            if(StringUtils.isNotBlank(nameContainsVal)){
                String[] arr = nameContainsVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.nameContains(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameContains(arr[i]));
                }
            }
            if(StringUtils.isNotBlank(nameMatchesVal)){
                String[] arr = nameMatchesVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.nameMatches(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameMatches(arr[i]));
                }
            }
            if(StringUtils.isNotBlank(isAnnotatedWith)){
                String[] arr = isAnnotatedWith.split(",");
                for(int i = 0; i < arr.length; i++) {
                    if(includeMatcher == null){
                        includeMatcher = ElementMatchers.isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i]));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i])));
                }
            }
            if(includeMatcher != null){
                matcher = matcher.and(includeMatcher);
            }
        }

        if(excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String isAnnotatedWith = excludeMap.get("isAnnotatedWith");
            if(StringUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(arr[i])));
                }
            }
            if(StringUtils.isNotBlank(nameStartsWithVal)){
                String[] arr = nameStartsWithVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameStartsWith(arr[i])));
                }
            }
            if(StringUtils.isNotBlank(nameEndsWithVal)){
                String[] arr = nameEndsWithVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameEndsWith(arr[i])));
                }
            }
            if(StringUtils.isNotBlank(nameContainsVal)){
                String[] arr = nameContainsVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameContains(arr[i])));
                }
            }
            if(StringUtils.isNotBlank(nameMatchesVal)){
                String[] arr = nameMatchesVal.split(",");
                for(int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameMatches(arr[i])));
                }
            }
            if(StringUtils.isNotBlank(isAnnotatedWith)) {
                String[] arr = isAnnotatedWith.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
        }
        return matcher;
    }
}
